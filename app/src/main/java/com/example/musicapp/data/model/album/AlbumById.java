package com.example.musicapp.data.model.album;

import com.google.gson.annotations.SerializedName;

public class AlbumById {
    @SerializedName("data")
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
