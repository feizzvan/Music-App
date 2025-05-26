package com.example.musicapp.ui.home.album.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.databinding.FragmentDetailAlbumBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.utils.SharedDataUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailAlbumFragment extends AppBaseFragment {
    private FragmentDetailAlbumBinding mBinding;
    private DetailAlbumViewModel mDetailAlbumViewModel;
    private SongListAdapter mSongListAdapter;

    @Inject
    DetailAlbumViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDetailAlbumBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDetailAlbumViewModel =
                new ViewModelProvider(requireActivity(), factory).get(DetailAlbumViewModel.class);

        setupAlbumData();
        setupView();
        setupViewModel();
    }

    private void setupAlbumData() {
        DetailAlbumFragmentArgs args = DetailAlbumFragmentArgs.fromBundle(getArguments());
//        mDetailAlbumViewModel.setAlbum(args.getId(), args.getTitle(), args.getCoverImageUrl());
        mDetailAlbumViewModel.loadAlbumById(args.getId());
    }

    private void setupView() {
        mBinding.includeFragmentDetailPlaylist
                .toolbarPlaylistDetail
                .setNavigationOnClickListener(
                        view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        mSongListAdapter = new SongListAdapter(
                (song, index) -> {
                    Album album = mDetailAlbumViewModel.getAlbum().getValue();
                    String playlistName = album == null ? " " : album.getTitle();
                    SharedDataUtils.addPlaylist(mDetailAlbumViewModel.getPlaylist());
                    showAndPlay(song, index, playlistName);
                }, this::showOptionMenu);

        mBinding.includeFragmentDetailPlaylist
                .includeSongList
                .rvSongList
                .setAdapter(mSongListAdapter);
    }

    private void setupViewModel() {
        mDetailAlbumViewModel.getAlbum()
                .observe(getViewLifecycleOwner(), this::showAlbumInfo);

        mDetailAlbumViewModel.getSongs().observe(getViewLifecycleOwner(), mSongListAdapter::updateSongs);

//        mDetailAlbumViewModel.loadAlbumById(mDetailAlbumViewModel.getAlbum().getValue().getId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(songList -> {
//                    List<Song> songs = songList.getSongs();
//                    mSongListAdapter.updateSongs(songs);
//                    mDetailAlbumViewModel.setSongs(songs);
//                    mDetailAlbumViewModel.createPlaylist(mDetailAlbumViewModel.getAlbum().getValue().getTitle());
//                }, throwable -> {
//                });

//        mDetailAlbumViewModel.getAlbumSongs()
//                .observe(getViewLifecycleOwner(), mSongListAdapter::updateSongs);
    }

    private void showAlbumInfo(Album album) {
        mBinding.includeFragmentDetailPlaylist.textTitlePlaylistDetail.setText(album.getTitle());
        String numberSong = getString(R.string.text_number_song, album.getSongs().size());
        mBinding.includeFragmentDetailPlaylist.textNumberSongPlaylist.setText(numberSong);
        Glide.with(this)
                .load(album.getCoverImageUrl())
                .error(R.drawable.ic_album)
                .into(mBinding.includeFragmentDetailPlaylist.imgPlaylistAvatar);
    }
}