package com.example.musicapp.ui.discovery.mostheard;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.MOST_HEARD;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.databinding.FragmentMostHeardBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.discovery.DiscoveryFragmentDirections;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.ui.discovery.mostheard.more.MoreMostHeardViewModel;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MostHeardFragment extends AppBaseFragment {
    private FragmentMostHeardBinding mBinding;
    private MostHeardViewModel mMostHeardViewModel;
    private MoreMostHeardViewModel  mMoreMostHeardViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    MostHeardViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMostHeardBinding.inflate(inflater, container, false);
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
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = MOST_HEARD.getValue();
                    showAndPlay(song, index, playlistName);
                }, this::showOptionMenu
        );
        mBinding.includeMostHeard.rvSongList.setAdapter(mAdapter);
        mBinding.textTitleMostHeard.setOnClickListener(view -> navigateToMoreMostHeard());
        mBinding.btnMoreMostHeard.setOnClickListener(view -> navigateToMoreMostHeard());
    }

    private void setupViewModel() {
        mMostHeardViewModel =
                new ViewModelProvider(requireActivity(), factory).get(MostHeardViewModel.class);
        mMoreMostHeardViewModel =
                new ViewModelProvider(requireActivity()).get(MoreMostHeardViewModel.class);

        mMostHeardViewModel.getSongs().observe(getViewLifecycleOwner(), mAdapter::updateSongs);
        mDisposable.add(mMostHeardViewModel.loadTopMostHeardSong()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    mAdapter.updateSongs(songs);
                    mMostHeardViewModel.setSongs(songs);

                    SharedDataUtils.setupPlaylist(songs, MOST_HEARD.getValue());
                }, throwable -> mMostHeardViewModel.setSongs(new ArrayList<>()))
        );
    }

    private void navigateToMoreMostHeard() {
        NavDirections direction = DiscoveryFragmentDirections.actionDiscoveryFrToMoreMostHeardFr();
        NavHostFragment.findNavController(this).navigate(direction);
    }
}