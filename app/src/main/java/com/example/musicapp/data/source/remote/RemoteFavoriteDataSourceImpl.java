package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.R;
import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteByUserId;
import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.source.FavoriteDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteFavoriteDataSourceImpl implements FavoriteDataSource {

    @Inject
    public RemoteFavoriteDataSourceImpl() {
    }

    @Override
    public Single<Favorite> addFavorite(FavoriteRequest favoriteRequest) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<Favorite> call = appService.addFavorite(favoriteRequest);

            call.enqueue(new Callback<Favorite>() {
                public void onResponse(@NonNull Call<Favorite> call, @NonNull Response<Favorite> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception((R.string.favorite_add_failed) + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Favorite> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    @Override
    public Single<Favorite> removeFavorite(FavoriteRequest favoriteRequest) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<Favorite> call = appService.removeFavorite(favoriteRequest);

            call.enqueue(new Callback<Favorite>() {
                public void onResponse(@NonNull Call<Favorite> call, @NonNull Response<Favorite> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception((R.string.favorite_remove_failed) + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Favorite> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    @Override
    public Single<FavoriteByUserId> getFavorites(int userId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<FavoriteByUserId> call = appService.getFavoriteByUserId(userId);

            call.enqueue(new Callback<FavoriteByUserId>() {
                public void onResponse(@NonNull Call<FavoriteByUserId> call,
                                       @NonNull Response<FavoriteByUserId> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception((R.string.favorite_get_failed) + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavoriteByUserId> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }
}
