package com.example.musicapp.ui.playing;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.MediaSession;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.PlayingSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.song.SongRepository;
import com.example.musicapp.databinding.ActivityNowPlayingBinding;
import com.example.musicapp.service.MusicPlaybackService;
import com.example.musicapp.ui.dialog.optionmenu.OptionMenuViewModel;
import com.example.musicapp.ui.dialog.SongOptionMenuDialogFragment;
import com.example.musicapp.utils.AppUtils;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class NowPlayingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NowPlayingActivity";
    private ActivityNowPlayingBinding mBinding;
    private NowPlayingViewModel mNowPlayingViewModel;
//    private MediaSession mMediaSession;
        private MediaController mMediaSession;
    private Player.Listener mPlayerListener;
    private Handler mHandler;
    private Runnable mCallback;
    private Animator mAnimator;
    private ObjectAnimator mRotationAnimator;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    SongRepository.Local localSongRepository;

    @Inject
    TokenManager tokenManager;

    private final ServiceConnection mMusicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                MusicPlaybackService.LocalBinder binder = (MusicPlaybackService.LocalBinder) iBinder;
                binder.isMediaControllerInitialized().observe(NowPlayingActivity.this, isInitialized -> {
                    if (isInitialized) {
                        mMediaSession = binder.getMediaSession();
                        if (mMediaSession != null) {
                            Log.d(TAG, "MediaSession initialized successfully");
                            setupController();
                            updateSeekBar();
                            updateSeekBarMaxValue();
                            updateDuration();
                        } else {
                            Log.e(TAG, "MediaSession is null after initialization");
                            Toast.makeText(NowPlayingActivity.this, "MediaSession not available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "MediaController not initialized yet");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Service connection failed: " + e.getMessage(), e);
                Toast.makeText(NowPlayingActivity.this, "Service connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMediaSession = null;
            Log.w(TAG, "Service disconnected unexpectedly");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNowPlayingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(mBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(mBinding.nowPlaying, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupToolbar();
        setupAnimator();
        setupViewModel();
        setupView();

        // Observe favorite song ids for UI update
        SharedDataUtils.getFavoriteSongIdsLiveData().observe(this, ids -> {
            PlayingSong playingSong = SharedDataUtils.getPlayingSong().getValue();
            if (playingSong != null && playingSong.getSong() != null) {
                boolean isFavorite = ids != null && ids.contains(playingSong.getSong().getId());
                showFavoriteMode(isFavorite);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicPlaybackService.class);
        try {
            bindService(intent, mMusicServiceConnection, BIND_AUTO_CREATE);
            Log.d(TAG, "Binding to MusicPlaybackService");
        } catch (Exception e) {
            Log.e(TAG, "Cannot bind to MusicPlaybackService: " + e.getMessage(), e);
            Toast.makeText(this, "Cannot bind to MusicPlaybackService", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unbindService(mMusicServiceConnection);
            Log.d(TAG, "Unbound from MusicPlaybackService");
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Service was not bound: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaSession != null) {
            mMediaSession.removeListener(mPlayerListener);
            mMediaSession = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mCallback);
        }
        mPlayerListener = null;
        mDisposable.clear();
        Log.d(TAG, "Activity destroyed");
    }

    @Override
    public void onClick(View view) {
        mAnimator.setTarget(view);
        mAnimator.start();
        if (view.getId() == R.id.btn_play_pause) {
            setupActionPlayPause();
        } else if (view.getId() == R.id.btn_skip_previous) {
            setupActionSkipPrevious();
        } else if (view.getId() == R.id.btn_skip_next) {
            setupActionSkipNext();
        } else if (view.getId() == R.id.btn_repeat) {
            setupActionRepeat();
        } else if (view.getId() == R.id.btn_shuffle) {
            setupActionShuffle();
        } else if (view.getId() == R.id.btn_now_playing_favorite) {
            setupActionFavorite();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbarNowPlaying);
        mBinding.toolbarNowPlaying.setNavigationOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(AppUtils.EXTRA_CURRENT_FRACTION, mRotationAnimator.getAnimatedFraction());
            setResult(RESULT_OK, intent);
            getOnBackPressedDispatcher().onBackPressed();
        });
        mBinding.btnNowPlayingMoreOption.setOnClickListener(v -> showOptionMenuDialog());
    }

    private void showOptionMenuDialog() {
        OptionMenuViewModel optionMenuViewModel =
                new ViewModelProvider(this).get(OptionMenuViewModel.class);
        Song song = null;
        if (SharedDataUtils.getPlayingSong().getValue() != null) {
            song = SharedDataUtils.getPlayingSong().getValue().getSong();
        } else {
            Log.w(TAG, "No playing song available for option menu");
        }
        optionMenuViewModel.setSong(song);
        SongOptionMenuDialogFragment dialogFragment = SongOptionMenuDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), SongOptionMenuDialogFragment.TAG);
    }

    private void setupAnimator() {
        mAnimator = AnimatorInflater.loadAnimator(this, R.animator.button_pressed);
        mRotationAnimator = ObjectAnimator.ofFloat(mBinding.imageNowPlayingArtwork, "rotation", 0f, 360f);
        mRotationAnimator.setDuration(16000);
        mRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotationAnimator.setInterpolator(new LinearInterpolator());

        float currentFraction = getIntent().getFloatExtra(AppUtils.EXTRA_CURRENT_FRACTION, 0f);
        mRotationAnimator.setCurrentFraction(currentFraction);
    }

    private void setupViewModel() {
        mNowPlayingViewModel = new ViewModelProvider(this).get(NowPlayingViewModel.class);

        SharedDataUtils.getPlayingSong().observe(this, playingSong -> {
            if (playingSong != null) {
                Song song = playingSong.getSong();
                showSongInfo(song);
            } else {
                mBinding.textNowPlayingSongTitle.setText("No Song");
                mBinding.textNowPlayingSongArtist.setText("Unknown");
                mBinding.imageNowPlayingArtwork.setImageResource(R.drawable.ic_album);
                Log.w(TAG, "Playing song is null");
            }
        });
        mNowPlayingViewModel.isPlaying().observe(this, isPlaying -> {
            if (isPlaying != null && isPlaying) {
                if (mRotationAnimator.isPaused()) {
                    mRotationAnimator.resume();
                } else if (!mRotationAnimator.isRunning()) {
                    mRotationAnimator.start();
                }
                mBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            } else {
                mBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
                if (mRotationAnimator.isRunning()) {
                    mRotationAnimator.pause();
                }
            }
        });

    }

    private void setupView() {
        mBinding.btnPlayPause.setOnClickListener(this);
        mBinding.btnSkipPrevious.setOnClickListener(this);
        mBinding.btnSkipNext.setOnClickListener(this);
        mBinding.btnRepeat.setOnClickListener(this);
        mBinding.btnShuffle.setOnClickListener(this);
        mBinding.btnNowPlayingFavorite.setOnClickListener(this);

        mBinding.seekBarNowPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String currentTimeLabel = mNowPlayingViewModel.getTimeLabel(progress);
                mBinding.textCurrentDuration.setText(currentTimeLabel);
                if (fromUser && mMediaSession != null) {
                    mMediaSession.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

//    private void setupController() {
//        if (mMediaSession == null) {
//            Log.e(TAG, "MediaSession is null in setupController");
//            Toast.makeText(this, "MediaSession not initialized", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        registerMediaController();
//        try {
//            String playlistName = SharedDataUtils.getCurrentPlaylistName();
//            Integer index = SharedDataUtils.getIndexToPlay().getValue();
//            if (playlistName == null) {
//                Log.e(TAG, "Playlist name is null");
//                Toast.makeText(this, "No playlist selected", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (index == null) {
//                index = 0;
//                Log.w(TAG, "IndexToPlay is null, defaulting to 0");
//            }
//            if (!mMediaSession.getPlayer().isPlaying()) {
//                MusicPlaybackService service = MusicPlaybackService.getInstance();
//                if (service != null) {
//                    Log.d(TAG, "Playing playlist: " + playlistName + ", index: " + index);
//                    service.setPlaylistAndPlay(index, playlistName);
//                } else {
//                    Log.e(TAG, "MusicPlaybackService instance is null");
//                    Toast.makeText(this, "Playback service not available", Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (mMediaSession.getPlayer().isPlaying()) {
//                mNowPlayingViewModel.setIsPlaying(true);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Cannot play song: " + e.getMessage(), e);
//            Toast.makeText(this, "Cannot play song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void setupController() {
        registerMediaController();
        if (mMediaSession != null) {
            if (!mMediaSession.isPlaying()) {
                mMediaSession.prepare();
                mMediaSession.play();
            }
            if (mMediaSession.isPlaying()) {
                mNowPlayingViewModel.setIsPlaying(true);
            }
        }
    }

//    private void registerMediaController() {
//        if (mMediaSession == null) {
//            Log.e(TAG, "MediaSession is null in registerMediaController");
//            return;
//        }
//        mPlayerListener = new Player.Listener() {
//            @Override
//            public void onIsPlayingChanged(boolean isPlaying) {
//                mNowPlayingViewModel.setIsPlaying(isPlaying);
//            }
//
//            @Override
//            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
//                updateSeekBarMaxValue();
//                updateDuration();
//                if (mMediaSession != null) {
//                    Player player = mMediaSession.getPlayer();
//                    if (player != null) {
//                        if (player.isPlaying()) {
//                            mRotationAnimator.start();
//                        }
//                        int currentIndex = player.getCurrentMediaItemIndex();
//                        SharedDataUtils.setIndexToPlay(currentIndex);
//                        Log.d(TAG, "Media item transitioned, new index: " + currentIndex);
//                    }
//                }
//            }
//
//            @Override
//            public void onPlaybackStateChanged(int playbackState) {
//                if (playbackState == Player.STATE_READY) {
//                    updateSeekBarMaxValue();
//                    updateDuration();
//                }
//            }
//        };
//        mMediaSession.getPlayer().addListener(mPlayerListener);
//    }

    private void registerMediaController() {
        if (mMediaSession != null) {
            mPlayerListener = new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    mNowPlayingViewModel.setIsPlaying(isPlaying);
                }

                @Override
                public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                    updateSeekBarMaxValue();
                    updateDuration();
                    if (mMediaSession.isPlaying()) {
                        mRotationAnimator.start();
                    }
                }

                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_READY) {
                        updateSeekBarMaxValue();
                        updateDuration();
                    }
                }
            };
            mMediaSession.addListener(mPlayerListener);
        }
    }

    private void showSongInfo(Song song) {
        if (song != null) {
            updateSeekBarMaxValue();
            updateDuration();
            showRepeatMode();
            showShuffleMode();

            boolean isFavorite = mNowPlayingViewModel.isFavorite(song.getId());
            showFavoriteMode(isFavorite);
//            mBinding.textNowPlayingAlbum.setText(song.getAlbum);
            mBinding.textNowPlayingSongTitle.setText(song.getTitle());
            mBinding.textNowPlayingSongArtist
                    .setText(song.getArtistName() != null ? String.valueOf(song.getArtistName()) : "Unknown");
            Glide.with(this)
                    .load(song.getImageUrl())
                    .circleCrop()
                    .error(R.drawable.ic_album)
                    .into(mBinding.imageNowPlayingArtwork);
        } else {
            mBinding.textNowPlayingSongTitle.setText("No Song");
            mBinding.textNowPlayingSongArtist.setText("Unknown");
            mBinding.imageNowPlayingArtwork.setImageResource(R.drawable.ic_album);
            Log.w(TAG, "Song is null in showSongInfo");
        }
    }

    private void setupActionPlayPause() {
        if (mMediaSession != null) {
            if (mMediaSession.isPlaying()) {
                mMediaSession.pause();
            } else {
                mMediaSession.play();
            }
        } else {
            Log.e(TAG, "MediaSession is null in setupActionPlayPause");
        }
    }

    private void setupActionSkipPrevious() {
        if (mMediaSession != null && mMediaSession.hasPreviousMediaItem()) {
            mMediaSession.seekToPreviousMediaItem();
            mRotationAnimator.end();
        } else {
            Log.w(TAG, "No previous media item or MediaSession is null");
        }
    }

    private void setupActionSkipNext() {
        if (mMediaSession != null && mMediaSession.hasNextMediaItem()) {
            mMediaSession.seekToNextMediaItem();
            mRotationAnimator.end();
        } else {
            Log.w(TAG, "No next media item or MediaSession is null");
        }
    }

    private void setupActionRepeat() {
        if (mMediaSession != null) {
            int repeatMode = mMediaSession.getRepeatMode();
            switch (repeatMode) {
                case Player.REPEAT_MODE_OFF:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                    mMediaSession.setRepeatMode(Player.REPEAT_MODE_ONE);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_all);
                    mMediaSession.setRepeatMode(Player.REPEAT_MODE_ALL);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                    mMediaSession.setRepeatMode(Player.REPEAT_MODE_OFF);
                    break;
            }
        } else {
            Log.e(TAG, "MediaSession is null in setupActionRepeat");
        }
    }

    private void showRepeatMode() {
        if (mMediaSession != null) {
            int repeatMode = mMediaSession.getRepeatMode();
            switch (repeatMode) {
                case Player.REPEAT_MODE_OFF:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_all);
                    break;
            }
        }
    }

    private void setupActionShuffle() {
        if (mMediaSession != null) {
            boolean isShuffle = mMediaSession.getShuffleModeEnabled();
            mMediaSession.setShuffleModeEnabled(!isShuffle);
            showShuffleMode();
        } else {
            Log.e(TAG, "MediaSession is null in setupActionShuffle");
        }
    }

    private void showShuffleMode() {
        if (mMediaSession != null) {
            boolean isShuffle = mMediaSession.getShuffleModeEnabled();
            mBinding.btnShuffle.setImageResource(isShuffle ? R.drawable.ic_shuffle_on : R.drawable.ic_shuffle_off);
        }
    }

    private void setupActionFavorite() {
        PlayingSong playingSong = SharedDataUtils.getPlayingSong().getValue();
        Song song = null;

        if (playingSong != null) {
            song = playingSong.getSong();
        } else {
            Log.w(TAG, "No playing song available for favorite action");
        }

        if (song != null) {
            int userId = tokenManager.getUserId();
            int songId = song.getId();
            Log.d("FAVORITE", "Add userId=" + userId + ", songId=" + songId);
            boolean isFavorite = mNowPlayingViewModel.isFavorite(songId);

            if (isFavorite) {
                // Đã yêu thích, gọi API xóa
                mDisposable.add(mNowPlayingViewModel.removeFavorite(userId, songId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    mNowPlayingViewModel.removeFavoriteId(songId);
                                    showFavoriteMode(false);
                                    Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> {
                                    Toast.makeText(this, "Bỏ yêu thích thất bại!", Toast.LENGTH_SHORT).show();
                                }
                        )
                );
            } else {
                // Chưa yêu thích, gọi API thêm
                mDisposable.add(mNowPlayingViewModel.addFavorite(userId, songId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        response -> {
                                            mNowPlayingViewModel.addFavoriteId(songId);
                                            showFavoriteMode(true);
                                            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                        },
                                        throwable -> {
                                            Log.e(TAG, "Favorite add error: ", throwable);
                                            Toast.makeText(this, "Thêm vào yêu thích thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                )
                );
            }
        }
    }

    private void showFavoriteMode(boolean isFavorite) {
        mBinding.btnNowPlayingFavorite
                .setImageResource(isFavorite ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off);
    }

    private void updateSeekBar() {
        mHandler = new Handler();
        mCallback = new Runnable() {
            @Override
            public void run() {
                if (mMediaSession != null) {
                    int currentPos = (int) mMediaSession.getCurrentPosition();
                    mBinding.seekBarNowPlaying.setProgress(currentPos);
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(mCallback);
    }

    private void updateSeekBarMaxValue() {
        if (mMediaSession != null) {
            long duration = mMediaSession.getDuration();
            int seekBarMaxValue = duration <= Integer.MAX_VALUE ? (int) duration : 0;
            int progress = (int) mMediaSession.getCurrentPosition();
            mBinding.seekBarNowPlaying.setProgress(progress);
            mBinding.seekBarNowPlaying.setMax(seekBarMaxValue);
        }
    }

    private void updateDuration() {
        if (mMediaSession != null) {
            String timeLabel = mNowPlayingViewModel.getTimeLabel(mMediaSession.getDuration());
            mBinding.textTotalDuration.setText(timeLabel);
        }
    }
}
