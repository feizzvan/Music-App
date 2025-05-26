package com.example.musicapp.ui.library.playlist.more;

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

import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.databinding.FragmentMorePlaylistBinding;
import com.example.musicapp.ui.library.playlist.PlaylistAdapter;
import com.example.musicapp.ui.library.playlist.PlaylistViewModel;
import com.example.musicapp.ui.library.playlist.detail.PlaylistDetailViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MorePlaylistFragment extends Fragment {
    private FragmentMorePlaylistBinding mBinding;
    private PlaylistAdapter mAdapter;
    private PlaylistViewModel mPlaylistViewModel;
    private PlaylistDetailViewModel mPlaylistDetailViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public PlaylistViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMorePlaylistBinding.inflate(inflater, container, false);
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
        mDisposable.clear();
    }

    private void setupView() {
        mBinding.toolbarMorePlaylist.setNavigationOnClickListener(view ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mAdapter = new PlaylistAdapter(
                this::loadPlaylist,
                playlist -> {

                });
        mBinding.rvMorePlaylist.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        mPlaylistViewModel =
                new ViewModelProvider(requireActivity(), factory).get(PlaylistViewModel.class);
        MorePlaylistViewModel morePlaylistViewModel =
                new ViewModelProvider(requireActivity()).get(MorePlaylistViewModel.class);
        morePlaylistViewModel.getPlaylistLiveData().observe(getViewLifecycleOwner(),
                playlists -> mAdapter.updatePlaylists(playlists));
        mPlaylistDetailViewModel =
                new ViewModelProvider(requireActivity()).get(PlaylistDetailViewModel.class);
    }

    private void loadPlaylist(Playlist playlist) {
        navigateToPlaylistDetail(playlist);
    }

    private void navigateToPlaylistDetail(Playlist playlist) {
        MorePlaylistFragmentDirections.ActionMorePlaylistFrToPlaylistDetailFr action =
                MorePlaylistFragmentDirections.actionMorePlaylistFrToPlaylistDetailFr();
        action.setId(playlist.getId());
        action.setTitle(playlist.getName());
        action.setUserId(playlist.getUserId());
        NavHostFragment.findNavController(this).navigate(action);
    }
}