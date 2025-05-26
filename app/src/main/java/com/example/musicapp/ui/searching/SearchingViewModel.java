package com.example.musicapp.ui.searching;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.SEARCHED;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.searching.SearchingRepository;
import com.example.musicapp.utils.SharedDataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class SearchingViewModel extends ViewModel {
    private final SearchingRepository mSearchingRepository;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();
    private final MutableLiveData<String> mSelectedKey = new MutableLiveData<>();

    @Inject
    public SearchingViewModel(SearchingRepository searchingRepository) {
        mSearchingRepository = searchingRepository;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setSong(List<Song> songs) {
        mSongs.postValue(songs);
    }

    public Flowable<List<Song>> search(String key) {
        return mSearchingRepository.search(key);
    }

    // Cập nhật lại danh sách bài hát trong playlist đã tìm kiếm gần đây - tạo lịch sử tìm kiếm
    public void updatePlaylist(Song song) {
        String playlistName = SEARCHED.getValue();
        Playlist playlist = SharedDataUtils.getPlaylist(playlistName);
        List<Song> songs = new ArrayList<>();

        if (playlist != null && !playlist.getSongs().isEmpty()) {
            songs.addAll(playlist.getSongs());
        }

        if (songs.isEmpty()) {
            songs.add(song);
        } else {
            songs.remove(song);
            songs.add(0, song);
        }

        if (playlist != null) {
            playlist.updateSongs(songs);
            SharedDataUtils.addPlaylist(playlist);
        }
    }

    public Completable insertKeys(String key) {
        List<HistorySearchedKey> keys = new ArrayList<>();
        keys.add(new HistorySearchedKey(0, key, new Date()));
        return mSearchingRepository.insertKeys(keys);
    }

    public Completable insertSongs(Song song) {
        List<HistorySearchedSong> songs = new ArrayList<>();
        HistorySearchedSong historySearchedSong = new HistorySearchedSong.Builder(song).build();
        songs.add(historySearchedSong);
        return mSearchingRepository.insertSongs(songs);
    }

    public Flowable<List<HistorySearchedKey>> getAllKeys() {
        return mSearchingRepository.getAllKeys();
    }

    public Flowable<List<Song>> getHistorySearchedSongs() {
        return mSearchingRepository.getHistorySearchedSongs().map(ArrayList::new);
    }

    public LiveData<String> getSelectedKey() {
        return mSelectedKey;
    }

    public void setSelectedKey(String key) {
        mSelectedKey.setValue(key);
    }

    public Completable clearAllKeys() {
        return mSearchingRepository.clearAllKeys();
    }

    public Completable clearAllSongs() {
        return mSearchingRepository.clearAllSongs();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SearchingRepository mSearchingRepository;

        @Inject
        public Factory(SearchingRepository searchingRepository) {
            mSearchingRepository = searchingRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SearchingViewModel.class)) {
                return (T) new SearchingViewModel(mSearchingRepository);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}