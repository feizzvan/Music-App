package com.example.musicapp.ui.library;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl;
import com.example.musicapp.data.repository.recent.RecentSongRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class LibraryViewModel extends ViewModel {
    private final RecentSongRepository mRecentSongRepository;
    private final PlaylistRepositoryImpl mPlaylistRepository;

    private final MutableLiveData<List<Song>> mRecentSongs = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> mFavoriteSongs = new MutableLiveData<>();

    @Inject
    public LibraryViewModel(RecentSongRepository recentSongRepository,
                            PlaylistRepositoryImpl playlistRepository) {
        mRecentSongRepository = recentSongRepository;
        mPlaylistRepository = playlistRepository;
    }

    // Tải danh sách bài hát gần đây từ một nguồn dữ liệu thông qua RecentSongRepository
    public Flowable<List<Song>> loadRecentSongs() {
        return mRecentSongRepository.getAllRecentSongs().map(ArrayList::new); // Kết quả trả về là một ArrayList
    }

    public void setRecentSongs(List<Song> recentSongs) {
        mRecentSongs.postValue(recentSongs);
    }

    public LiveData<List<Song>> getRecentSongs() {
        return mRecentSongs;
    }

    public void setFavoriteSongs(List<Song> favoriteSongs) {
        mFavoriteSongs.postValue(favoriteSongs);
    }

    public LiveData<List<Song>> getFavoriteSongs() {
        return mFavoriteSongs;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final RecentSongRepository mRecentSongRepository;
        private final PlaylistRepositoryImpl mPlaylistRepository;

        @Inject
        public Factory(RecentSongRepository recentSongRepository,
                       PlaylistRepositoryImpl playlistRepository) {
            mRecentSongRepository = recentSongRepository;
            mPlaylistRepository = playlistRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LibraryViewModel.class)) {
                return (T) new LibraryViewModel(mRecentSongRepository, mPlaylistRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }


    }
}