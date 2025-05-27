package com.example.musicapp.ui.playing;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;

import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.favorite.FavoriteRepository;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MiniPlayerViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final MutableLiveData<List<MediaItem>> mMediaItems = new MutableLiveData<>();
    private final FavoriteRepository favoriteRepository;
    private final TokenManager tokenManager;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public MiniPlayerViewModel(FavoriteRepository favoriteRepository, TokenManager tokenManager) {
        this.favoriteRepository = favoriteRepository;
        this.tokenManager = tokenManager;
    }

    public void addFavoriteAndSync(Song song) {
        int userId = tokenManager.getUserId();
        mDisposable.add(addFavorite(userId, song.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fav -> {
                    SharedDataUtils.addFavoriteSong(song);
                }, throwable -> {
                    // handle error
                })
        );

    }

    public void removeFavoriteAndSync(Song song) {
        int userId = tokenManager.getUserId();
        mDisposable.add(removeFavorite(userId, song.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fav -> {
                    SharedDataUtils.removeFavoriteSong(song.getId());
                }, throwable -> {
                    // handle error
                })
        );
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        mMediaItems.setValue(mediaItems);
    }

    public LiveData<List<MediaItem>> getMediaItems() {
        return mMediaItems;
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<Boolean> isPlaying() {
        return mIsPlaying;
    }

    public Single<Favorite> addFavorite(int userId, int songId) {
        String token = "Bearer " + tokenManager.getToken();
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return favoriteRepository.addFavorite(token, request);
    }

    public Single<Favorite> removeFavorite(int userId, int songId) {
        String token = "Bearer " + tokenManager.getToken();
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return favoriteRepository.removeFavorite(token, request);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FavoriteRepository mFavoriteRepository;
        private final TokenManager tokenManager;

        public Factory(FavoriteRepository favoriteRepository, TokenManager tokenManager) {
            mFavoriteRepository = favoriteRepository;
            this.tokenManager = tokenManager;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MiniPlayerViewModel.class)) {
                return (T) new MiniPlayerViewModel(mFavoriteRepository, tokenManager);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
