package com.example.musicapp.ui.discovery.artist.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.data.model.artist.Artist;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MoreArtistViewModel extends ViewModel {
    private final MutableLiveData<List<Artist>> mArtists = new MutableLiveData<>();

    @Inject
    public MoreArtistViewModel() {
    }

    public LiveData<List<Artist>> getArtists() {
        return mArtists;
    }

    public void setArtists(List<Artist> artists) {
        mArtists.postValue(artists);
    }
}