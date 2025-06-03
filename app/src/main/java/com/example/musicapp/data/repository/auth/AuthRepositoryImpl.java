package com.example.musicapp.data.repository.auth;

import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.UpdatePasswordRequest;
import com.example.musicapp.data.source.AuthDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthDataSource mRemoteDataSource;

    @Inject
    public AuthRepositoryImpl(AuthDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<AuthenticationResponse> updatePassword(String token, UpdatePasswordRequest updatePasswordRequest) {
        return mRemoteDataSource.updatePassword(token, updatePasswordRequest);
    }

    @Override
    public Single<AuthenticationResponse> logout(LogoutRequest logoutRequest) {
        return mRemoteDataSource.logout(logoutRequest);
    }
}
