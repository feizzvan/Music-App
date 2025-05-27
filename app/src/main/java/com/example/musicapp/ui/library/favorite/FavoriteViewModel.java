package com.example.musicapp.ui.library.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.favorite.FavoriteRepository;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository mFavoriteRepository;
    private final TokenManager tokenManager;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public FavoriteViewModel(FavoriteRepository favoriteRepository, TokenManager tokenManager) {
        mFavoriteRepository = favoriteRepository;
        this.tokenManager = tokenManager;
    }

    public void loadFavoriteSongs(int userId) {
        String token = "Bearer " + tokenManager.getToken();
        mDisposable.add(mFavoriteRepository.getFavorites(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteListResponse -> {
                            List<Song> songs = favoriteListResponse.getData() != null
                                    ? favoriteListResponse.getData().getSongs()
                                    : null;

                            SharedDataUtils.setFavoriteSongs(songs);
                        },
                        throwable -> {
                        }
                )
        );
    }

    public Completable addSongToFavorite(int userId, int songId) {
        String token = "Bearer " + tokenManager.getToken();
        return mFavoriteRepository.addFavorite(token, new FavoriteRequest(userId, songId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ignoreElement();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FavoriteRepository mFavoriteRepository;
        private final TokenManager tokenManager;

        @Inject
        public Factory(FavoriteRepository favoriteRepository, TokenManager tokenManager) {
            mFavoriteRepository = favoriteRepository;
            this.tokenManager = tokenManager;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
                return (T) new FavoriteViewModel(mFavoriteRepository, tokenManager);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}