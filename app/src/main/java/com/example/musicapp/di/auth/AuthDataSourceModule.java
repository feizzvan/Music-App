package com.example.musicapp.di.auth;

import com.example.musicapp.data.source.AuthDataSource;
import com.example.musicapp.data.source.remote.RemoteAuthDataSourceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AuthDataSourceModule {
    @Binds
    public abstract AuthDataSource bindRemoteAuthDataSource(RemoteAuthDataSourceImpl remoteAuthDataSource);
}
