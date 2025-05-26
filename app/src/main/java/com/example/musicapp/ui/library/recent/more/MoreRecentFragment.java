package com.example.musicapp.ui.library.recent.more;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.RECENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.databinding.FragmentMoreRecentBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;

public class MoreRecentFragment extends AppBaseFragment {
    private FragmentMoreRecentBinding mBinding;
    private SongListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreRecentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.toolbarMoreRecent.setNavigationOnClickListener(view ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = RECENT.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeSongList.rvSongList.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        MoreRecentViewModel moreRecentViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecentViewModel.class);
        moreRecentViewModel.getRecentSongs().observe(getViewLifecycleOwner(), songs ->
                mAdapter.updateSongs(songs));
    }
}