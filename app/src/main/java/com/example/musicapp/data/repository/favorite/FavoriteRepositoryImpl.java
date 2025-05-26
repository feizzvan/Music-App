package com.example.musicapp.data.repository.favorite;

import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteByUserId;
import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.source.FavoriteDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoriteDataSource mRemoteDataSource;

    @Inject
    public FavoriteRepositoryImpl(FavoriteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<Favorite> addFavorite(FavoriteRequest favoriteRequest) {
        return mRemoteDataSource.addFavorite(favoriteRequest);
    }

    @Override
    public Single<Favorite> removeFavorite(FavoriteRequest favoriteRequest) {
        return mRemoteDataSource.removeFavorite(favoriteRequest);
    }

    @Override
    public Single<FavoriteByUserId> getFavorites(int userId) {
        return mRemoteDataSource.getFavorites(userId);
    }
}
