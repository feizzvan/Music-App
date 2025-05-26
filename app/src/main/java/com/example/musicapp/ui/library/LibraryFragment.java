package com.example.musicapp.ui.library;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.FAVOURITE;
import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.RECENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.data.repository.recent.RecentSongRepository;
import com.example.musicapp.databinding.FragmentLibraryBinding;
import com.example.musicapp.ui.library.favorite.FavoriteViewModel;
import com.example.musicapp.ui.library.playlist.PlaylistViewModel;
import com.example.musicapp.ui.library.recent.RecentSongViewModel;
import com.example.musicapp.ui.searching.SearchingFragmentDirections;
import com.example.musicapp.utils.SharedDataUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class LibraryFragment extends Fragment {
    public static final String SCROLL_POSITION = "com.example.musicapp.ui.library.SCROLL_POSITION";
    private FragmentLibraryBinding mBinding;
    private LibraryViewModel mLibraryViewModel;
    private RecentSongViewModel mRecentSongViewModel;
    private FavoriteViewModel mFavoriteViewModel;
    private PlaylistViewModel mPlaylistViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private int scrollPosition = 0;

    @Inject
    public LibraryViewModel.Factory libraryFactory;

    @Inject
    public PlaylistViewModel.Factory playlistFactory;

    @Inject
    public FavoriteViewModel.Factory favoriteFactory;

    @Inject
    public RecentSongRepository recentSongRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.scrollViewLibrary.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            scrollPosition = scrollY;
        });

        // Khôi phục vị trí cuộn
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0);
            mBinding.scrollViewLibrary.post(() -> mBinding.scrollViewLibrary.setScrollY(scrollPosition));
        }


        setupView();
        setupViewModel();
        setupObserver();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Lưu trữ vị trí cuộn của ScrollView
        outState.putInt(SCROLL_POSITION, scrollPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    private void setupView() {
        mBinding.btnLibrarySearch.setOnClickListener(view -> {
            NavDirections directions = SearchingFragmentDirections.actionGlobalFrSearching();
            NavHostFragment.findNavController(this).navigate(directions);
        });
    }

    private void setupViewModel() {
        mLibraryViewModel =
                new ViewModelProvider(requireActivity(), libraryFactory).get(LibraryViewModel.class);
        mRecentSongViewModel =
                new ViewModelProvider(requireActivity()).get(RecentSongViewModel.class);
        mFavoriteViewModel =
                new ViewModelProvider(requireActivity(), favoriteFactory).get(FavoriteViewModel.class);
        mPlaylistViewModel =
                new ViewModelProvider(requireActivity(), playlistFactory).get(PlaylistViewModel.class);

        mLibraryViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            mFavoriteViewModel.setFavoriteSongs(songs);
            mLibraryViewModel.setFavoriteSongs(songs);
            SharedDataUtils.setupPlaylist(songs, FAVOURITE.getValue());
        });

        mLibraryViewModel.getRecentSongs().observe(getViewLifecycleOwner(), songs -> {
            mRecentSongViewModel.setRecentSongs(songs);
            SharedDataUtils.setupPlaylist(songs, RECENT.getValue());
        });
    }

    private void setupObserver() {
        mDisposable.add(SharedDataUtils.loadRecentSongs(recentSongRepository)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> mLibraryViewModel.setRecentSongs(songs),
                        throwable -> mLibraryViewModel.setRecentSongs(null))
        );

//        mDisposable.add(mLibraryViewModel.loadPlaylistWithSongs()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(playlists -> mPlaylistViewModel.setPlaylists(playlists),
//                        throwable -> mPlaylistViewModel.setPlaylists(null))
//        );
    }
}