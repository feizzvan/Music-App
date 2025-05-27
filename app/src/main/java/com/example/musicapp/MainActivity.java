package com.example.musicapp;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.SEARCHED;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.musicapp.data.model.PlayingSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.ui.library.playlist.PlaylistViewModel;
import com.example.musicapp.utils.AppUtils;
import com.example.musicapp.utils.PermissionUtils;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    public static final String PREF_SONG_ID = "com.example.musicapp.PREF_SONG_ID";
    public static final String PREF_PLAYLIST_NAME = "com.example.musicapp.PREF_PLAYLIST_NAME";

    private ActivityMainBinding mBinding;
    private PlaylistViewModel mPlaylistViewModel;
    private SharedPreferences mSharedPreferences;

    private boolean isFirstLoad = true;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public PlaylistViewModel.Factory playlistViewModelFactory;

    @Inject
    public MainViewModel.Factory mMainViewModelFactory;

    @Inject
    public TokenManager tokenManager;

    private final ActivityResultLauncher<String> mResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    PermissionUtils.setPermissionGranted(true);
                } else {
                    Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                            R.string.message_permission_denied,
                            Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(R.id.bottom_nav_view);
                    snackbar.show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);

        setContentView(mBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(mBinding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupToolbar();
        setupViewModel();
        setupSharedPreferences();
        setupComponents();
        observeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedDataUtils.getPlayingSong().observe(this, playingSong -> {
            if (playingSong != null) {
                if (playingSong.getSong() != null) { //Nếu bài hát có cả ở local và ở remote
                    mBinding.fcvMiniPlayer.setVisibility(View.VISIBLE);
                } else {
                    mBinding.fcvMiniPlayer.setVisibility(View.GONE);
                }
            } else {
                mBinding.fcvMiniPlayer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupToolbar() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(mBinding.bottomNavView, navController);
        }
    }

    private void setupViewModel() {
        mPlaylistViewModel =
                new ViewModelProvider(this, playlistViewModelFactory).get(PlaylistViewModel.class);

        SharedDataUtils.isSongLoaded().observe(this, isLoaded -> {
            if (isLoaded && isFirstLoad) {
                readPrefPlayingSong();
                isFirstLoad = false;
            }
        });

        PermissionUtils.getPermissionAsked().observe(this, isAsked -> {
            if (isAsked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermission();
                }
            }
        });
    }

    private void setupSharedPreferences() {
        mSharedPreferences = getApplicationContext()
                .getSharedPreferences("MUSIC_APP_PREFERENCES", MODE_PRIVATE);
    }

    private void setupComponents() {
        //Thiết lập giá trị xdpi trong AppUtils để sử dụng trong toàn ứng dụng
        // bằng cách lấy mật độ điểm ảnh theo trục X của màn hình thiết bị
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        AppUtils.X_DPI = displayMetrics.xdpi;
    }

    private void observeData() {
//        mDisposable.add(mPlaylistViewModel.getAllPlaylistWithSongs()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        playlistWithSongs -> {
//                            mPlaylistViewModel.setPlaylists(playlistWithSongs);
//                            SharedDataUtils.setPlaylistSongs(playlistWithSongs);
//                        },
//                        error -> {
//                        }
//                )
//        );

        MainViewModel mainViewModel = new ViewModelProvider(this, mMainViewModelFactory)
                .get(MainViewModel.class);
        mDisposable.add(mainViewModel.loadHistorySearchedSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    if (songs != null && !songs.isEmpty()) {
                        SharedDataUtils.setupPlaylist(songs, SEARCHED.getValue());
                    }
                }, throwable -> {
                }));
    }

    private void saveCurrentSong() {
        PlayingSong playingSong = SharedDataUtils.getPlayingSong().getValue();
        if (playingSong != null) {
            Song song = playingSong.getSong();
            if (song != null) {
                mSharedPreferences.edit()
                        .putInt("PREF_SONG_ID", song.getId())
                        .putString("PREF_PLAYLIST_NAME", playingSong.getPlaylist().getName())
                        .apply();
            }
        }
    }

    private void readPrefPlayingSong() {
        String songId = mSharedPreferences.getString(PREF_SONG_ID, null);
        String playlistName = mSharedPreferences.getString(PREF_PLAYLIST_NAME, null);
        if (songId != null && playlistName != null) {
            SharedDataUtils.setupPreviousSessionPlayingSong(songId, playlistName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU) //Đảm bảo cho android 13 trở lên
    private void checkPermission() {
        final String permission = android.Manifest.permission.POST_NOTIFICATIONS;
        boolean isGranted = ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            PermissionUtils.setPermissionGranted(true);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // Kiểm tra xem hệ thống có nên hiển thị lời giải thích về quyền trước khi yêu cầu lại không
            Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                    R.string.permission_description,
                    Snackbar.LENGTH_LONG);
            snackbar.setAnchorView(R.id.bottom_nav_view);
            snackbar.setAction(R.string.action_agree, v -> mResultLauncher.launch(permission));
            snackbar.show();
        } else {
            mResultLauncher.launch(permission);
        }
    }
}
