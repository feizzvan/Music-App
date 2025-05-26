package com.example.musicapp.data.model.favorite;

import com.google.gson.annotations.SerializedName;

public class FavoriteByUserId {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Favorite data;

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

    public Favorite getData() {
        return data;
    }

    public void setData(Favorite data) {
        this.data = data;
    }
}
