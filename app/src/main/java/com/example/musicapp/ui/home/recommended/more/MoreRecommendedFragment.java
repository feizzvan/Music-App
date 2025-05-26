package com.example.musicapp.ui.home.recommended.more;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.RECOMMENDED;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentMoreRecommendedBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;

public class MoreRecommendedFragment extends AppBaseFragment {
    private FragmentMoreRecommendedBinding mBinding;
    private SongListAdapter mSongListAdapter;
    private MoreRecommendedViewModel mMoreRecommendedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreRecommendedBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.includeToolbar
                .toolbar
                .setNavigationOnClickListener(
                        view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mBinding.includeToolbar.textToolbarTitle.setText(R.string.title_recommended_songs);
        
        mSongListAdapter = new SongListAdapter(
                (song, index) -> showAndPlay(song, index, RECOMMENDED.getValue()),
                this::showOptionMenu);

        mBinding.includeSongList.rvSongList.setAdapter(mSongListAdapter);
    }

    private void setupViewModel() {
        mMoreRecommendedViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecommendedViewModel.class);
        mMoreRecommendedViewModel.getSongs()
                .observe(getViewLifecycleOwner(), mSongListAdapter::updateSongs);

    }
}