package com.example.musicapp.ui.home.recommended;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.DEFAULT;
import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.RECOMMENDED;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentRecommendedBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.ui.home.HomeFragmentDirections;
import com.example.musicapp.ui.home.album.detail.DetailAlbumViewModel;
import com.example.musicapp.ui.home.recommended.more.MoreRecommendedViewModel;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class RecommendedFragment extends AppBaseFragment {
    private FragmentRecommendedBinding mBinding;
    private SongListAdapter mSongListAdapter;
    private RecommendedSongViewModel mRecommendedSongViewModel;
    private MoreRecommendedViewModel mMoreRecommendedViewModel;
    private DetailAlbumViewModel mDetailAlbumViewModel;

    CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public RecommendedSongViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRecommendedBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDisposable.dispose();
    }

    private void setupView() {
        mBinding.progressRecommendedSong.setVisibility(View.VISIBLE);
        mSongListAdapter = new SongListAdapter(
                (song, index) -> showAndPlay(song, index, RECOMMENDED.getValue()),
                this::showOptionMenu);
        mBinding.includeRecommendedSongs.rvSongList.setAdapter(mSongListAdapter);
        mBinding.btnMoreRecommendedSongs.setOnClickListener(view -> navigateToMoreRecommended());
        mBinding.textTitleRecommendedSongs.setOnClickListener(view -> navigateToMoreRecommended());
    }

    private void setupViewModel() {
        mDetailAlbumViewModel =
                new ViewModelProvider(requireActivity()).get(DetailAlbumViewModel.class);
        mRecommendedSongViewModel =
                new ViewModelProvider(this, factory).get(RecommendedSongViewModel.class);
        mMoreRecommendedViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecommendedViewModel.class);
        mRecommendedSongViewModel.loadRecommendedSongs();
        mRecommendedSongViewModel.getSongList().observe(getViewLifecycleOwner(), songs -> {
//            saveSongToDB(songs);
            mDetailAlbumViewModel.setSongs(songs);
            mSongListAdapter.updateSongs(songs.subList(0, Math.min(15, songs.size())));
            mMoreRecommendedViewModel.setSongs(songs);
            SharedDataUtils.setupPlaylist(songs, DEFAULT.getValue());
            SharedDataUtils.setupPlaylist(songs, RECOMMENDED.getValue());
            mBinding.progressRecommendedSong.setVisibility(View.GONE);
            SharedDataUtils.setSongLoaded(true);
        });
    }

//    private void saveSongToDB(List<Song> songs) {
//        mDisposable.add(mRecommendedSongViewModel.saveSongToDB(songs)
//                .subscribeOn(Schedulers.io())
//                .subscribe());
//    }

    private void navigateToMoreRecommended() {
        NavDirections direction = HomeFragmentDirections.actionHomeFrToMoreRecommendedFr();
        NavHostFragment.findNavController(this).navigate(direction);
    }

}