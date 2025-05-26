package com.example.musicapp.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class PlaylistById {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Playlist data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Playlist getData() {
        return data;
    }

    public void setData(Playlist data) {
        this.data = data;
    }
}