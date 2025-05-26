package com.example.musicapp.data.model.favorite;

import com.google.gson.annotations.SerializedName;

public class FavoriteRequest {
    @SerializedName("userId")
    private int userId;

    @SerializedName("songId")
    private int songId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(int userId, int songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
