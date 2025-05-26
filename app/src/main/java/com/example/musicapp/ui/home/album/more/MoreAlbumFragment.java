package com.example.musicapp.ui.home.album.more;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.databinding.FragmentMoreAlbumBinding;
import com.example.musicapp.ui.home.album.AlbumViewModel;
import com.example.musicapp.ui.home.album.detail.DetailAlbumViewModel;

public class MoreAlbumFragment extends Fragment {
    private FragmentMoreAlbumBinding mBinding;
    private MoreAlbumAdapter mMoreAlbumAdapter;
    private AlbumViewModel mAlbumViewModel;
    private DetailAlbumViewModel mDetailAlbumViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreAlbumBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.toolbarMoreAlbum.setNavigationOnClickListener(
                view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mMoreAlbumAdapter = new MoreAlbumAdapter(album -> {
            mDetailAlbumViewModel.setAlbum(album.getId(), album.getTitle(), album.getCoverImageUrl());
            //mDetailAlbumViewModel.extractSongList(album);
            navigateToDetailAlbum(album);
        });
        mBinding.recyclerMoreAlbum.setAdapter(mMoreAlbumAdapter);
    }

    private void setupViewModel() {
        mAlbumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
        mDetailAlbumViewModel = new ViewModelProvider(requireActivity()).get(DetailAlbumViewModel.class);
        mAlbumViewModel.getAlbumList().observe(getViewLifecycleOwner(), mMoreAlbumAdapter::updateAlbums);
    }

    private void navigateToDetailAlbum(Album album) {
        MoreAlbumFragmentDirections.ActionMoreAlbumFrToDetailAlbumFr action =
                MoreAlbumFragmentDirections.actionMoreAlbumFrToDetailAlbumFr();
        action.setId(album.getId());
        action.setTitle(album.getTitle());
        action.setCoverImageUrl(album.getCoverImageUrl());
        NavHostFragment.findNavController(this).navigate(action);
    }
}