package com.example.musicapp.data.model.history;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Search {
    @SerializedName("data")
    private List<Song> songs;

    public Search() {
    }

    public List<Song> getSongs() {
        return songs != null ? songs : new ArrayList<>();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
