package com.example.musicapp.ui.library.playlist.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.databinding.FragmentMorePlaylistBinding;
import com.example.musicapp.ui.library.playlist.PlaylistAdapter;
import com.example.musicapp.ui.library.playlist.PlaylistViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MorePlaylistFragment extends Fragment {
    private FragmentMorePlaylistBinding mBinding;
    private PlaylistAdapter mAdapter;
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
                this::navigateToPlaylistDetail,
                playlist -> {

                });
        mBinding.rvMorePlaylist.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        MorePlaylistViewModel morePlaylistViewModel =
                new ViewModelProvider(requireActivity()).get(MorePlaylistViewModel.class);
        morePlaylistViewModel.getPlaylistLiveData().observe(getViewLifecycleOwner(),
                playlists -> mAdapter.updatePlaylists(playlists));
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