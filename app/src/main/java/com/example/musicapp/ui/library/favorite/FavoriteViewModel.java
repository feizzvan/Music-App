package com.example.musicapp.ui.library.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.favorite.FavoriteRepository;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FavoriteViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mFavoriteSongs = new MutableLiveData<>();
    private final FavoriteRepository mFavoriteRepository;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public FavoriteViewModel(FavoriteRepository favoriteRepository) {
        mFavoriteRepository = favoriteRepository;
    }

    public void loadFavoriteSongs(int userId) {
        mDisposable.add(mFavoriteRepository.getFavorites(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteListResponse -> {
                            List<Song> songs = favoriteListResponse.getData() != null
                                    ? favoriteListResponse.getData().getSongs()
                                    : null;
                            mFavoriteSongs.setValue(songs);

                            // Cập nhật SharedDataUtils để các nơi khác cùng đồng bộ
                            List<Integer> ids = new ArrayList<>();
                            if (songs != null) {
                                for (Song song : songs) {
                                    ids.add(song.getId());
                                }
                            }
                            SharedDataUtils.setFavoriteSongIds(ids);
                        },
                        throwable -> mFavoriteSongs.setValue(null)
                )
        );
    }

    public Completable addSongToFavorite(int userId, int songId) {
        return mFavoriteRepository.addFavorite(new FavoriteRequest(userId, songId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ignoreElement();
    }

    public void setFavoriteSongs(List<Song> favoriteSongs) {
        mFavoriteSongs.setValue(favoriteSongs);
    }

    public LiveData<List<Song>> getFavoriteSongs() {
        return mFavoriteSongs;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FavoriteRepository mFavoriteRepository;

        @Inject
        public Factory(FavoriteRepository favoriteRepository) {
            mFavoriteRepository = favoriteRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
                return (T) new FavoriteViewModel(mFavoriteRepository);
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