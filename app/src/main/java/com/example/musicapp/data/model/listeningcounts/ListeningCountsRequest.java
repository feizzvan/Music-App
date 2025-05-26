package com.example.musicapp.data.model.listeningcounts;

import com.google.gson.annotations.SerializedName;

public class ListeningCountsRequest {
    @SerializedName("userId")
    private int userId;

    @SerializedName("songId")
    private int songId;

    public ListeningCountsRequest() {
    }

    public ListeningCountsRequest(int userId, int songId) {
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
