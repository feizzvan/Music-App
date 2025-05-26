package com.example.musicapp.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {
    @SerializedName("email")
    private String mEmail;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
