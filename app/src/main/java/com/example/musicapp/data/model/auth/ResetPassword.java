package com.example.musicapp.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class ResetPassword {
    @SerializedName("token")
    private String token;

    @SerializedName("newPassword")
    private String newPassword;

    public ResetPassword() {
    }

    public ResetPassword(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
