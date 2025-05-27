package com.example.musicapp.ui.library.favorite;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.FAVOURITE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentFavoriteBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.ui.library.LibraryFragmentDirections;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoriteFragment extends AppBaseFragment {
    private FragmentFavoriteBinding mBinding;
    private FavoriteViewModel mFavoriteViewModel;
    private SongListAdapter mAdapter;

    @Inject
    public TokenManager tokenManager;

    @Inject
    public FavoriteViewModel.Factory favoriteFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();

        int userId = tokenManager.getUserId();
        mFavoriteViewModel =
                new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        mFavoriteViewModel.loadFavoriteSongs(userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void setupView() {
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = FAVOURITE.getValue();
                    List<Song> favoriteSongs = SharedDataUtils.getFavoriteSongsLiveData().getValue();
                    SharedDataUtils.setupPlaylist(favoriteSongs, playlistName);
                    SharedDataUtils.setCurrentPlaylist(playlistName);
                    SharedDataUtils.setIndexToPlay(index);
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeFavorite.rvSongList.setAdapter(mAdapter);
        mBinding.textTitleFavorite.setOnClickListener(view -> navigateToMoreFavorite());
        mBinding.btnMoreFavorite.setOnClickListener(view -> navigateToMoreFavorite());
    }

    private void setupViewModel() {
        mFavoriteViewModel =
                new ViewModelProvider(requireActivity(), favoriteFactory).get(FavoriteViewModel.class);

        SharedDataUtils.getFavoriteSongsLiveData().observe(getViewLifecycleOwner(), songs -> {
            List<Song> subList = new ArrayList<>();
            if (songs != null)
                subList.addAll(songs.size() < 10 ? songs : songs.subList(0, 10));
            mAdapter.updateSongs(subList);
        });
    }

    private void navigateToMoreFavorite() {
        NavDirections directions = LibraryFragmentDirections.actionLibraryFrToMoreFavoriteFr();
        NavHostFragment.findNavController(this).navigate(directions);
    }
}