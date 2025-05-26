package com.example.musicapp.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class LogoutRequest {
    @SerializedName("userId")
    private int userId;

    public LogoutRequest() {
    }

    public LogoutRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
