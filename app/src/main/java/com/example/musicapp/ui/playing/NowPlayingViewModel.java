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
import com.example.musicapp.utils.TokenManager;

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
    private final TokenManager tokenManager;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public NowPlayingViewModel(FavoriteRepositoryImpl favoriteRepository,
                               ListeningCountsRepositoryImpl listeningCountsRepository,
                               TokenManager tokenManager) {
        mFavoriteRepository = favoriteRepository;
        mListeningCountsRepository = listeningCountsRepository;
        this.tokenManager = tokenManager;
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

    public void addFavoriteAndSync(Song song) {
        int userId = tokenManager.getUserId();
        mDisposable.add(addFavorite(userId, song.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fav -> SharedDataUtils.addFavoriteSong(song),
                        throwable -> {
                        }
                )
        );

    }

    public void removeFavoriteAndSync(Song song) {
        int userId = tokenManager.getUserId();
        mDisposable.add(removeFavorite(userId, song.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fav -> SharedDataUtils.removeFavoriteSong(song.getId()),
                        throwable -> {
                        }
                )
        );
    }

    public Single<Favorite> addFavorite(int userId, int songId) {
        String token = "Bearer " + tokenManager.getToken();
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return mFavoriteRepository.addFavorite(token, request);
    }

    public Single<Favorite> removeFavorite(int userId, int songId) {
        String token = "Bearer " + tokenManager.getToken();
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return mFavoriteRepository.removeFavorite(token, request);
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
        private final TokenManager tokenManager;

        @Inject
        public Factory(FavoriteRepositoryImpl favoriteRepository,
                       ListeningCountsRepositoryImpl listeningCountsRepository,
                       TokenManager tokenManager) {
            mFavoriteRepository = favoriteRepository;
            mListeningCountsRepository = listeningCountsRepository;
            this.tokenManager = tokenManager;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NowPlayingViewModel.class)) {
                return (T) new NowPlayingViewModel(mFavoriteRepository,
                        mListeningCountsRepository,
                        tokenManager);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
