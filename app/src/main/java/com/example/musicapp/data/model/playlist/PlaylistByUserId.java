package com.example.musicapp.data.model.playlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistByUserId {
    @SerializedName("data")
    private List<Playlist> playlists;

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylist(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
