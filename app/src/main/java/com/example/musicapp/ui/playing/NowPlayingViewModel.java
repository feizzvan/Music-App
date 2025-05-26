package com.example.musicapp.ui.playing;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsRequest;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.favorite.FavoriteRepositoryImpl;
import com.example.musicapp.data.repository.listeningcounts.ListeningCountsRepositoryImpl;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class NowPlayingViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final FavoriteRepositoryImpl mFavoriteRepository;
    private final ListeningCountsRepositoryImpl mListeningCountsRepository;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public NowPlayingViewModel(FavoriteRepositoryImpl favoriteRepository,
                               ListeningCountsRepositoryImpl listeningCountsRepository) {
        mFavoriteRepository = favoriteRepository;
        mListeningCountsRepository = listeningCountsRepository;
    }

    public void increaseListeningCount(int userId, int songId) {
        mDisposable.add(mListeningCountsRepository.addListeningCounts(new ListeningCountsRequest(userId, songId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                        },
                        throwable -> {
                        }
                )
        );
    }

    public boolean isFavorite(int songId) {
        List<Integer> ids = SharedDataUtils.getFavoriteSongIdsLiveData().getValue();
        return ids != null && ids.contains(songId);
    }

    public void syncFavoriteSongIds(int userId) {
        mDisposable.add(mFavoriteRepository.getFavorites(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteListResponse -> {
                    List<Integer> songIds = new ArrayList<>();
                    List<Song> songs = favoriteListResponse.getData().getSongs() != null ? favoriteListResponse.getData().getSongs() : null;
                    if (songs != null) {
                        for (Song song : songs) {
                            songIds.add(song.getId());
                        }
                    }
                    SharedDataUtils.setFavoriteSongIds(songIds);
                }, throwable -> {
                })
        );
    }

    public void addFavoriteId(int songId) {
        List<Integer> ids = SharedDataUtils.getFavoriteSongIdsLiveData().getValue();
        if (ids == null) ids = new ArrayList<>();
        if (!ids.contains(songId)) {
            ids.add(songId);
            SharedDataUtils.setFavoriteSongIds(ids);
        }
    }

    public void removeFavoriteId(int songId) {
        List<Integer> ids = SharedDataUtils.getFavoriteSongIdsLiveData().getValue();
        if (ids != null && ids.contains(songId)) {
            ids.remove(Integer.valueOf(songId));
            SharedDataUtils.setFavoriteSongIds(ids);
        }
    }

    public Single<Favorite> addFavorite(int userId, int songId) {
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return mFavoriteRepository.addFavorite(request);
    }

    public Single<Favorite> removeFavorite(int userId, int songId) {
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return mFavoriteRepository.removeFavorite(request);
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<Boolean> isPlaying() {
        return mIsPlaying;
    }

    public String getTimeLabel(long duration) {
        long minutes = duration / 1000 / 60;
        long seconds = duration / 1000 % 60;
        if (duration < 0 || duration > Integer.MAX_VALUE) {
            return "00:00";
        }
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FavoriteRepositoryImpl mFavoriteRepository;
        private final ListeningCountsRepositoryImpl mListeningCountsRepository;

        @Inject
        public Factory(FavoriteRepositoryImpl favoriteRepository,
                       ListeningCountsRepositoryImpl listeningCountsRepository) {
            mFavoriteRepository = favoriteRepository;
            mListeningCountsRepository = listeningCountsRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NowPlayingViewModel.class)) {
                return (T) new NowPlayingViewModel(mFavoriteRepository,
                        mListeningCountsRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
