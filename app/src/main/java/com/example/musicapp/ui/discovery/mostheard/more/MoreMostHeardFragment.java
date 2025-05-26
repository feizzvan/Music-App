package com.example.musicapp.ui.discovery.mostheard.more;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.MOST_HEARD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.databinding.FragmentMoreMostHeardBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.ui.discovery.mostheard.MostHeardViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MoreMostHeardFragment extends AppBaseFragment {
    private FragmentMoreMostHeardBinding mBinding;
    private SongListAdapter mAdapter;
    private MostHeardViewModel mMostHeardViewModel;

    @Inject
    public MostHeardViewModel.Factory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreMostHeardBinding.inflate(inflater, container, false);
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
    }

    private void setupView() {
        mBinding.toolbarMoreMostHeard.setNavigationOnClickListener(
                view -> requireActivity().getSupportFragmentManager().popBackStack());
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = MOST_HEARD.getValue();
                    showAndPlay(song, index, playlistName);
                }, this::showOptionMenu
        );
        mBinding.includeMoreMostHeardSongList.rvSongList.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        mMostHeardViewModel =
                new ViewModelProvider(requireActivity(), factory).get(MostHeardViewModel.class);
        mMostHeardViewModel.getSongs().observe(getViewLifecycleOwner(), mAdapter::updateSongs);

//        mDisposable.add(mMoreMostHeardViewModel.loadTop40MostHeardSongs()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(songs -> {
//                    mAdapter.updateSongs(songs);
//                    mMoreMostHeardViewModel.setSongs(songs);
//                    SharedDataUtils.setupPlaylist(songs, MOST_HEARD.getValue());
//                }, t -> {
//                    mAdapter.updateSongs(new ArrayList<>());
//                    mMoreMostHeardViewModel.setSongs(new ArrayList<>());
//                })
//        );
    }
}