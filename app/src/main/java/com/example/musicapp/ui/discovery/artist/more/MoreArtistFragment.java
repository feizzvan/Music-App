package com.example.musicapp.ui.discovery.artist.more;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.databinding.FragmentMoreArtistBinding;
import com.example.musicapp.ui.discovery.artist.ArtistAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MoreArtistFragment extends Fragment {
    private FragmentMoreArtistBinding mBinding;
    private ArtistAdapter mAdapter;
    private MoreArtistViewModel mMoreArtistViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreArtistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    private void setupView() {
        mAdapter = new ArtistAdapter(this::navigateToDetailArtist);
        mBinding.includeMoreArtist.rvArtist.setAdapter(mAdapter);
        mBinding.toolbarMoreArtist.setNavigationOnClickListener(
                view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupViewModel() {
        mMoreArtistViewModel = new ViewModelProvider(requireActivity()).get(MoreArtistViewModel.class);
        mMoreArtistViewModel.getArtists().observe(getViewLifecycleOwner(), mAdapter::updateArtist);
    }

    private void navigateToDetailArtist(Artist artist) {
        MoreArtistFragmentDirections.ActionMoreArtistFrToDetailArtistFr action
                = MoreArtistFragmentDirections.actionMoreArtistFrToDetailArtistFr();
        action.setArtistId(artist.getId());
        NavHostFragment.findNavController(this).navigate(action);
    }
}