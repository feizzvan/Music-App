package com.example.musicapp.data.source;

import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteByUserId;
import com.example.musicapp.data.model.favorite.FavoriteRequest;

import io.reactivex.rxjava3.core.Single;

public interface FavoriteDataSource {
    Single<Favorite> addFavorite(String token, FavoriteRequest favoriteRequest);

    Single<Favorite> removeFavorite(String token, FavoriteRequest favoriteRequest);

    Single<FavoriteByUserId> getFavorites(String token, int userId);
}
