package com.example.musicapp.ui.dialog.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.data.model.song.Song;

public class SongInfoDialogViewModel extends ViewModel {
    private final MutableLiveData<Song> mSong = new MutableLiveData<>();

    public void setSong(Song song) {
        mSong.setValue(song);
    }

    public LiveData<Song> getSong() {
        return mSong;
    }
}