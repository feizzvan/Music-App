package com.example.musicapp.ui.library.playlist.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentPlaylistDetailBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaylistDetailFragment extends AppBaseFragment {
    private FragmentPlaylistDetailBinding mBinding;
    private SongListAdapter mAdaper;
    private PlaylistDetailViewModel mPlaylistDetailViewModel;

    @Inject
    public PlaylistDetailViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPlaylistDetailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupPlaylistData();
        setupView();
    }

    private void setupPlaylistData() {
        PlaylistDetailFragmentArgs args = PlaylistDetailFragmentArgs.fromBundle(getArguments());
        mPlaylistDetailViewModel.loadPlaylistById(args.getId());
    }

    private void setupView() {
        mBinding.includePlaylistDetail.toolbarPlaylistDetail.setNavigationOnClickListener(
                view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        String toolBarTitle = getString(R.string.title_playlist_detail);
        mBinding.includePlaylistDetail.textToolbarTitle.setText(toolBarTitle);
        mAdaper = new SongListAdapter(
                (song, index) -> {
                    Playlist playlist = mPlaylistDetailViewModel.getPlaylist().getValue();
                    if (playlist != null) {
                        String playlistName = playlist.getName();
                        SharedDataUtils.addPlaylist(mPlaylistDetailViewModel.getPlaylistForPlayback());
                        SharedDataUtils.setCurrentPlaylist(playlistName);
                        SharedDataUtils.setIndexToPlay(index);
                        showAndPlay(song, index, playlistName);
                    }
                },
                this::showOptionMenu
        );
        mBinding.includePlaylistDetail.includeSongList.rvSongList.setAdapter(mAdaper);
    }

    private void setupViewModel() {
        mPlaylistDetailViewModel =
                new ViewModelProvider(requireActivity(), factory).get(PlaylistDetailViewModel.class);
        mPlaylistDetailViewModel.getPlaylist()
                .observe(getViewLifecycleOwner(), playlist -> {
                    if (playlist != null) {
                        mAdaper.updateSongs(playlist.getSongs() != null ? playlist.getSongs() : new ArrayList<>());
                        showPlaylistInfo(playlist);
                    }
                });
    }

    private void showPlaylistInfo(Playlist playlist) {
        String artworkUrl = null;
        if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
            artworkUrl = playlist.getSongs().get(0).getImageUrl();
        }
        Glide.with(this)
                .load(artworkUrl)
                .error(R.drawable.ic_album)
                .into(mBinding.includePlaylistDetail.imgPlaylistAvatar);
        String playlistName = playlist.getName();
        mBinding.includePlaylistDetail.textTitlePlaylistDetail.setText(playlistName);
        String numOfSong = getString(R.string.text_number_song, playlist.getSongs() != null ? playlist.getSongs().size() : 0);
        mBinding.includePlaylistDetail.textNumberSongPlaylist.setText(numOfSong);
    }
}
