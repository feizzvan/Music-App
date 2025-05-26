package com.example.musicapp.di.auth;

import com.example.musicapp.data.repository.auth.AuthRepository;
import com.example.musicapp.data.repository.auth.AuthRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AuthRepositoryModule {
    @Binds
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl authRepository);
}
