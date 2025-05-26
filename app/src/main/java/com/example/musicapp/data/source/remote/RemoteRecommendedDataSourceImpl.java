package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.data.model.recommended.TopRecommended;
import com.example.musicapp.data.source.RecommendedDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteRecommendedDataSourceImpl implements RecommendedDataSource {
    @Inject
    public RemoteRecommendedDataSourceImpl() {
    }

    @Override
    public Single<TopRecommended> getRecommendationSongs(int userId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<TopRecommended> call = appService.getRecommendations(userId);

            call.enqueue(new Callback<TopRecommended>() {
                @Override
                public void onResponse(@NonNull Call<TopRecommended> call,
                                       @NonNull Response<TopRecommended> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<TopRecommended> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}
