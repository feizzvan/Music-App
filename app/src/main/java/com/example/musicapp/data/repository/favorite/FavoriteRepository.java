package com.example.musicapp.data.repository.favorite;

import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteByUserId;
import com.example.musicapp.data.model.favorite.FavoriteRequest;

import io.reactivex.rxjava3.core.Single;

public interface FavoriteRepository {
    Single<Favorite> addFavorite(FavoriteRequest favoriteRequest);

    Single<Favorite> removeFavorite(FavoriteRequest favoriteRequest);

    Single<FavoriteByUserId> getFavorites(int userId);
}
