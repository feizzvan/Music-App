package com.example.musicapp.ui.dialog.optionmenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.utils.MenuOptionUtils;

import java.util.List;

public class OptionMenuViewModel extends ViewModel {
    private final MutableLiveData<List<OptionMenuItem>> mOptionMenuItem = new MutableLiveData<>();
    private final MutableLiveData<Song> mSong = new MutableLiveData<>();

    public LiveData<Song> getSong() {
        return mSong;
    }

    public LiveData<List<OptionMenuItem>> getOptionMenuItem() {
        return mOptionMenuItem;
    }

    public void setSong(Song song) {
        mSong.setValue(song);
        mOptionMenuItem.setValue(MenuOptionUtils.getSongOptionMenuItems(song));
    }
}
