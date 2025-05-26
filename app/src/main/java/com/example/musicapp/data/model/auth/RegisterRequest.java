package com.example.musicapp.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    private final String role = "CUSTOMER";
    @SerializedName("username")
    private String mFullName;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName) {
        mEmail = email;
        mPassword = password;
        mFullName = fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }
}
