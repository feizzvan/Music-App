package com.example.musicapp.ui.library.recent.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.data.model.song.Song;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MoreRecentViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mRecentSongs = new MutableLiveData<>();

    @Inject
    public MoreRecentViewModel() {
    }

    public void setRecentSongs(List<Song> recentSongs) {
        mRecentSongs.setValue(recentSongs);
    }

    public LiveData<List<Song>> getRecentSongs() {
        return mRecentSongs;
    }
}