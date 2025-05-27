package com.example.musicapp.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class PlaylistById {
    @SerializedName("ata")
    private Playlist data;

    public Playlist getData() {
        return data;
    }

    public void setData(Playlist data) {
        this.data = data;
    }
}