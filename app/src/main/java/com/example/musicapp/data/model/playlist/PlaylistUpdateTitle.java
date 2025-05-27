package com.example.musicapp.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class PlaylistUpdateTitle {
    @SerializedName("title")
    private String title;

    public PlaylistUpdateTitle() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
