package com.example.musicapp.ui.searching;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.SEARCHED;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.databinding.FragmentSearchingBinding;
import com.example.musicapp.ui.AppBaseFragment;
import com.example.musicapp.ui.SongListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SearchingFragment extends AppBaseFragment {
    private FragmentSearchingBinding mBinding;
    private SearchingViewModel mSearchingViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    SearchingViewModel.Factory mSearchingFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSearchingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupSearchView();
        setupViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDisposable.clear();
    }

    private void setupView() {
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    // save to local DB
                    mDisposable.add(mSearchingViewModel.insertSongs(song)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());

                    mSearchingViewModel.updatePlaylist(song);
                    mBinding.searchViewHome.clearFocus();
                    mBinding.getRoot().requestFocus();
                    String playlistName = SEARCHED.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );

        mBinding.includeSearchedResult.rvSearchedSong.setAdapter(mAdapter);
        mBinding.toolbarSearching.setNavigationOnClickListener(view -> {
            mSearchingViewModel.setSelectedKey("");
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }
private String textSearch = "";
    private void setupSearchView() {
        SearchManager manager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        mBinding.searchViewHome.setSearchableInfo(manager.getSearchableInfo(requireActivity().getComponentName()));
        mBinding.searchViewHome.setIconified(false);
        mBinding.searchViewHome.setSubmitButtonEnabled(true);
        mBinding.searchViewHome.setQueryRefinementEnabled(true);
        mBinding.searchViewHome.onActionViewExpanded();
        mBinding.searchViewHome.setMaxWidth(Integer.MAX_VALUE);
        mBinding.searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mRunnable = () -> {
        if (!textSearch.trim().isEmpty()) {
            activeSearchResultLayout(true);
            performSearch(textSearch);
            saveSearchedKey(textSearch);
        } else {
            activeSearchResultLayout(false);
        }
    };

    private void search(String textSearch) {
        this.textSearch = textSearch;
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 250L);
    }

    private void setupViewModel() {
        mSearchingViewModel = new ViewModelProvider(requireActivity(), mSearchingFactory)
                .get(SearchingViewModel.class);
        mSearchingViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
            String  json = new GsonBuilder().create().toJson(songs);
            Log.d("VANVAN2", "mSearchingViewModel: " + json);
            if (songs != null) {
                mAdapter.updateSongs(songs);
            }
        });

        mSearchingViewModel.getSelectedKey().observe(getViewLifecycleOwner(), key -> {
            if (key != null && !key.isEmpty()) {
                mBinding.searchViewHome.setQuery(key, false);
            }
        });
    }

    private void activeSearchResultLayout(boolean shouldShowSearchResult) {
        int searchResultVisibility;
        int historyVisibility;

        if (shouldShowSearchResult) {
            searchResultVisibility = View.VISIBLE;
            historyVisibility = View.GONE;
        } else {
            searchResultVisibility = View.GONE;
            historyVisibility = View.VISIBLE;
        }

        mBinding.includeSearchedResult.rvSearchedSong.setVisibility(searchResultVisibility);
        mBinding.includeSearchedHistory.fcvHistorySearchedKey.setVisibility(historyVisibility);
        mBinding.includeSearchedHistory.fcvHistorySearchedSongs.setVisibility(historyVisibility);
    }

    private void performSearch(String query) {
        mDisposable.add(mSearchingViewModel.search(query.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> mSearchingViewModel.setSong(songs),
                        throwable -> {
                            String message = String.valueOf(throwable.getMessage());
                            Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void saveSearchedKey(String key) {
        mDisposable.add(mSearchingViewModel.insertKeys(key.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }
}