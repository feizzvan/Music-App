package com.example.musicapp.ui.discovery.artist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentArtistBinding;
import com.example.musicapp.ui.discovery.DiscoveryFragmentDirections;
import com.example.musicapp.ui.discovery.artist.more.MoreArtistViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ArtistFragment extends Fragment {
    private FragmentArtistBinding mBinding;
    private ArtistAdapter mArtistAdapter;
    private ArtistViewModel mArtistViewModel;
    private MoreArtistViewModel mMoreArtistViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public ArtistViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentArtistBinding.inflate(inflater, container, false);
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
        mDisposable.dispose();
    }

    private void setupView() {
        mArtistAdapter = new ArtistAdapter(this::navigateToDetailArtist);
        mBinding.includeArtist.rvArtist.setAdapter(mArtistAdapter);
        mBinding.textTitleArtist.setOnClickListener(view -> navigateToMoreArtist());
        mBinding.btnMoreArtist.setOnClickListener(view -> navigateToMoreArtist());
    }

    private void setupViewModel() {
        mArtistViewModel =
                new ViewModelProvider(requireActivity(), factory).get(ArtistViewModel.class);
        mMoreArtistViewModel =
                new ViewModelProvider(requireActivity()).get(MoreArtistViewModel.class);

        mArtistViewModel.getArtist().observe(getViewLifecycleOwner(), artists -> {
            mArtistAdapter.updateArtist(artists);
            mMoreArtistViewModel.setArtists(artists);
            mDisposable.add(mArtistViewModel.saveArtistToLocalDB(artists)
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        });

//        mArtistViewModel.getArtist().observe(getViewLifecycleOwner(), artists ->
//                mDisposable.add(mArtistViewModel.saveArtistToLocalDB(artists)
//                        .subscribeOn(Schedulers.io())
//                        .subscribe()
//                ));
    }

    private void navigateToDetailArtist(Artist artist) {
        DiscoveryFragmentDirections.ActionDiscoveryFrToDetailArtistFr action =
                DiscoveryFragmentDirections.actionDiscoveryFrToDetailArtistFr();
        action.setArtistId(artist.getId());
        action.setArtistName(artist.getName());
        action.setArtistAvatar(artist.getAvatar());
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToMoreArtist() {
        NavDirections directions = DiscoveryFragmentDirections.actionDiscoveryFrToMoreArtistFr();
        NavHostFragment.findNavController(this).navigate(directions);
    }
}