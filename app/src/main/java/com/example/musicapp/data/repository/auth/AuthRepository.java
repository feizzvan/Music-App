package com.example.musicapp.data.repository.auth;

import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.ResetPassword;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<AuthenticationResponse> resetPassword(ResetPassword resetPassword);

    Single<AuthenticationResponse> logout(LogoutRequest logoutRequest);
}
