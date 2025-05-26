package com.example.musicapp.ui.dialog.information;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentSongInfoDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SongInfoDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "SongInfoDialogFragment";
    private FragmentSongInfoDialogBinding mBinding;

    private SongInfoDialogViewModel mViewModel;

    public static SongInfoDialogFragment newInstance() {
        return new SongInfoDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSongInfoDialogBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
    }

    private void setupViewModel(){
        mViewModel = new ViewModelProvider(requireActivity()).get(SongInfoDialogViewModel.class);
        mViewModel.getSong().observe(getViewLifecycleOwner(), this::showSongInfo);
    }

    private void showSongInfo(Song song) {
        Glide.with(this)
                .load(song.getImageUrl())
                .error(R.drawable.ic_music_note)
                .circleCrop()
                .into(mBinding.imgSongInfoAvatar);

        mBinding.textSongInfoTitle
                .setText(getString(R.string.text_song_info_title, song.getTitle()));
        mBinding.textSongInfoArtist
                .setText(getString(R.string.text_song_info_artist, song.getArtistName()));
//        mBinding.textSongInfoAlbum
//                .setText(getString(R.string.text_song_info_album, song.getAlbum()));
        mBinding.textSongInfoDuration
                .setText(getString(R.string.text_song_info_duration, song.getDuration()));
//        mBinding.textSongInfoCounter
//                .setText(getString(R.string.text_song_info_counter, song.getCounter()));
//        mBinding.textSongInfoReplay
//                .setText(getString(R.string.text_song_info_replay, song.getReplay()));
//        mBinding.textSongInfoFavorite
//                .setText(getString(R.string.text_song_info_favorite,
//                        song.isFavorite()
//                                ? getString(R.string.text_yes)
//                                : getString(R.string.text_no)));
        mBinding.textSongInfoGenre
                .setText(getString(R.string.text_song_info_genre, getString(R.string.text_na)));
        mBinding.textSongInfoPublishedYear
                .setText(getString(R.string.text_song_info_published_year, getString(R.string.text_na)));
    }
}