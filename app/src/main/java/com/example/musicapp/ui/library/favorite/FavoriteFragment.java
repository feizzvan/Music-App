package com.example.musicapp.ui.library.favorite;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.FAVOURITE;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentFavoriteBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.ui.library.LibraryFragmentDirections;
import com.example.musicapp.ui.library.LibraryViewModel;
import com.example.musicapp.ui.library.favorite.more.MoreFavoriteViewModel;
import com.example.musicapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoriteFragment extends AppBaseFragment {
    private FragmentFavoriteBinding mBinding;
    private MoreFavoriteViewModel mMoreFavoriteViewModel;
    private FavoriteViewModel mFavoriteViewModel;
    private LibraryViewModel mLibraryFavoriteViewModel;
    private SongListAdapter mAdapter;

    @Inject
    public TokenManager tokenManager;

    @Inject
    public FavoriteViewModel.Factory favoriteFactory;

    @Inject
    public LibraryViewModel.Factory libraryFactory;



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
        mMoreFavoriteViewModel =
                new ViewModelProvider(requireActivity()).get(MoreFavoriteViewModel.class);
        mLibraryFavoriteViewModel =
                new ViewModelProvider(requireActivity(), libraryFactory).get(LibraryViewModel.class);

        mFavoriteViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            mMoreFavoriteViewModel.setFavoriteSongs(songs);
            mLibraryFavoriteViewModel.setFavoriteSongs(songs);

            List<Song> subList = new ArrayList<>();
            if (songs != null)
                if (songs.size() < 10) {
                    subList.addAll(songs);
                } else {
                    subList.addAll(songs.subList(0, 10));
                }
            mAdapter.updateSongs(subList);
        });
    }

    private void navigateToMoreFavorite() {
        NavDirections directions = LibraryFragmentDirections.actionLibraryFrToMoreFavoriteFr();
        NavHostFragment.findNavController(this).navigate(directions);
    }
}