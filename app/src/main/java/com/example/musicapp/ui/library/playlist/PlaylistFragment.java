package com.example.musicapp.ui.library.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.databinding.FragmentPlaylistBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.dialog.playlist.PlaylistCreationDialog;
import com.example.musicapp.ui.library.LibraryFragmentDirections;
import com.example.musicapp.ui.library.playlist.detail.PlaylistDetailViewModel;
import com.example.musicapp.ui.library.playlist.more.MorePlaylistViewModel;
import com.example.musicapp.utils.AuthPromptUtils;
import com.example.musicapp.utils.TokenManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class PlaylistFragment extends AppBaseFragment {
    private FragmentPlaylistBinding mBinding;
    private PlaylistAdapter mAdapter;
    private PlaylistViewModel mPlaylistViewModel;
    private MorePlaylistViewModel mMorePlaylistViewModel;
    private PlaylistDetailViewModel mPlaylistDetailViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public PlaylistViewModel.Factory factory;

    @Inject
    public TokenManager tokenManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
        loadPlaylist();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupView() {
        mAdapter = new PlaylistAdapter(
                this::navigateToPlaylistDetail,
                this::showPlaylistOptionMenu
        );
        mBinding.rvPlaylist.setAdapter(mAdapter);
        mBinding.includeBtnAddPlaylist.btnAddPlaylist.setOnClickListener(view -> createPlaylist());
        mBinding.includeBtnAddPlaylist.textAddPlaylist.setOnClickListener(view -> createPlaylist());
        mBinding.btnMorePlaylist.setOnClickListener(view -> navigateToMorePlaylist());
        mBinding.textTitlePlaylist.setOnClickListener(view -> navigateToMorePlaylist());
    }

    private void setupViewModel() {
        mPlaylistViewModel = new ViewModelProvider(requireActivity(), factory).get(PlaylistViewModel.class);
        mMorePlaylistViewModel = new ViewModelProvider(requireActivity()).get(MorePlaylistViewModel.class);
        mPlaylistDetailViewModel = new ViewModelProvider(requireActivity()).get(PlaylistDetailViewModel.class);

        mPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && !playlists.isEmpty()) {
                int limit = Math.min(playlists.size(), 10);
                List<Playlist> subList = playlists.subList(0, limit);
                mAdapter.updatePlaylists(subList);
                mMorePlaylistViewModel.setPlaylistLiveData(playlists);
            }
        });
    }

    private void loadPlaylist() {
        int userId = tokenManager.getUserId();
        mPlaylistViewModel.loadPlaylistByUserId(userId);
    }

    private void createPlaylist() {
        if (tokenManager.getToken() == null) {
            AuthPromptUtils.showLoginRequiredDialog(requireContext());
            return;
        }

        PlaylistCreationDialog.PlaylistDialogListener listener = playlistName ->
                mDisposable.add(mPlaylistViewModel.createPlaylist(playlistName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createdPlaylist -> {
                            if (createdPlaylist != null && createdPlaylist.getData() != null) {
                                Toast.makeText(requireContext(), getString(R.string.playlist_created) + createdPlaylist.getData().getName(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), R.string.playlist_already_exists, Toast.LENGTH_SHORT).show();
                            }
                            mPlaylistViewModel.loadPlaylistByUserId(tokenManager.getUserId());
                        }, throwable -> {
                        })
                );

        PlaylistCreationDialog dialog = new PlaylistCreationDialog(listener);
        dialog.show(requireActivity().getSupportFragmentManager(), PlaylistCreationDialog.TAG);
    }

    private void navigateToMorePlaylist() {
        NavDirections directions = LibraryFragmentDirections.actionLibraryFrToMorePlaylistFr();
        NavHostFragment.findNavController(this).navigate(directions);
    }

    private void navigateToPlaylistDetail(Playlist playlist) {
        if (playlist.getId() <= 0) {
            Toast.makeText(requireContext(), "Invalid playlist ID", Toast.LENGTH_SHORT).show();
            return;
        }

        LibraryFragmentDirections.ActionLibraryFrToPlaylistDetailFr action =
                LibraryFragmentDirections.actionLibraryFrToPlaylistDetailFr();
        action.setId(playlist.getId());
        action.setTitle(playlist.getName());
        action.setUserId(playlist.getUserId());
        NavHostFragment.findNavController(this).navigate(action);
    }
}