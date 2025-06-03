package com.example.musicapp.ui.searching;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.SEARCHED;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.FragmentHistorySearchedSongBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.dialog.ConfirmationDialogFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HistorySearchedSongFragment extends AppBaseFragment {
    private FragmentHistorySearchedSongBinding mBinding;
    private SongListAdapter mAdapter;
    private SearchingViewModel mSearchingViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    SearchingViewModel.Factory mViewModelFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHistorySearchedSongBinding.inflate(inflater, container, false);
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
                    mBinding.getRoot().requestFocus();
                    String playlistName = SEARCHED.getValue();
                    SharedDataUtils.setupPlaylist(mAdapter.getSongs(), SEARCHED.getValue());
                    showAndPlay(song, index, playlistName);
                }, this::showOptionMenu
        );
        mBinding.rvHistorySearchedSong.setAdapter(mAdapter);

        mBinding.actionClearAllHistorySearchedSong.setOnClickListener(view -> {
            int messageId = R.string.message_confirm_clear_history_searched_songs;
            ConfirmationDialogFragment dialog = new ConfirmationDialogFragment(messageId, isConfirmed -> {
                if (isConfirmed) {
                    mDisposable.add(mSearchingViewModel.clearAllSongs()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());
                }
            });

            dialog.show(requireActivity().getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
        });
    }

    private void setupViewModel() {
        mSearchingViewModel = new ViewModelProvider(requireActivity(), mViewModelFactory)
                .get(SearchingViewModel.class);
        mDisposable.add(mSearchingViewModel.getHistorySearchedSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(song -> {
                    mAdapter.updateSongs(song);
                    SharedDataUtils.setupPlaylist(song, SEARCHED.getValue());
                }, throwable -> {

                }));
    }
}