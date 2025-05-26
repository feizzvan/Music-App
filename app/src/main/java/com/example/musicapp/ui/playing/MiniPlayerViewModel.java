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
import com.example.musicapp.data.model.song.SongList;
import com.example.musicapp.data.repository.favorite.FavoriteRepository;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;
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
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private final FavoriteRepository favoriteRepository;

    @Inject
    public MiniPlayerViewModel(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public boolean isFavorite(int songId) {
        List<Integer> ids = SharedDataUtils.getFavoriteSongIdsLiveData().getValue();
        return ids != null && ids.contains(songId);
    }

    public void syncFavoriteSongIds(int userId) {
        mDisposable.add(favoriteRepository.getFavorites(userId)
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
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return favoriteRepository.addFavorite(request);
    }

    public Single<Favorite> removeFavorite(int userId, int songId) {
        FavoriteRequest request = new FavoriteRequest(userId, songId);
        return favoriteRepository.removeFavorite(request);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FavoriteRepository favoriteRepository;

        public Factory(FavoriteRepository favoriteRepository) {
            this.favoriteRepository = favoriteRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MiniPlayerViewModel.class)) {
                return (T) new MiniPlayerViewModel(favoriteRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
