package com.example.musicapp.ui.library.favorite.more;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.FAVOURITE;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.databinding.FragmentMoreFavoriteBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MoreFavoriteFragment extends AppBaseFragment {
    private FragmentMoreFavoriteBinding mBinding;
    private SongListAdapter mSongListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreFavoriteBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    private void setupView() {
        mSongListAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = FAVOURITE.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeMoreFavoriteSongList.rvSongList.setAdapter(mSongListAdapter);
        mBinding.toolbarMoreFavorite.setNavigationOnClickListener(view ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupViewModel() {
        MoreFavoriteViewModel moreFavoriteViewModel =
                new ViewModelProvider(requireActivity()).get(MoreFavoriteViewModel.class);
        moreFavoriteViewModel.getFavoriteSongs()
                .observe(getViewLifecycleOwner(), songs -> mSongListAdapter.updateSongs(songs));
    }
}