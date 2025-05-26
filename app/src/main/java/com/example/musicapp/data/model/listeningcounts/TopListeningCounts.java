package com.example.musicapp.data.model.listeningcounts;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopListeningCounts {
    @SerializedName("data")
    private List<Song> data;

    public List<Song> getSongs() {
        return data;
    }

    public static class Data {
        @SerializedName("songs")
        private List<Song> songs;
    }
}
