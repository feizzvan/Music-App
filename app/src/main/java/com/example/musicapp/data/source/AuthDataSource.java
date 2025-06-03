package com.example.musicapp.data.source;

import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.UpdatePasswordRequest;

import io.reactivex.rxjava3.core.Single;

public interface AuthDataSource {
    Single<AuthenticationResponse> updatePassword(String token, UpdatePasswordRequest updatePasswordRequest);

    Single<AuthenticationResponse> logout(LogoutRequest logoutRequest);
}
