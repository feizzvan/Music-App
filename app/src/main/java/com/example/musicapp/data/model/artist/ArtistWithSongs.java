package com.example.musicapp.data.model.artist;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistWithSongs {
    @SerializedName("data")
    private Data data;

    public List<Song> getSongs() {
        if (data == null) {
            return null;
        }
        return data.songs;
    }

    public static class Data {
        @SerializedName("songs")
        private List<Song> songs;
    }
}
