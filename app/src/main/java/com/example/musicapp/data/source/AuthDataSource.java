package com.example.musicapp.data.source;

import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LoginRequest;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.ResetPassword;

import io.reactivex.rxjava3.core.Single;

public interface AuthDataSource {
    Single<AuthenticationResponse> resetPassword(ResetPassword resetPassword);

    Single<AuthenticationResponse> logout(LogoutRequest logoutRequest);
}
