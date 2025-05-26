package com.example.musicapp.data.source.remote;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicapp.R;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsResponse;
import com.example.musicapp.data.model.listeningcounts.TopListeningCounts;
import com.example.musicapp.data.source.ListeningCountsDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteListeningCountsDataSourceImpl implements ListeningCountsDataSource {

    @Inject
    public RemoteListeningCountsDataSourceImpl() {
    }

    @Override
    public Single<ListeningCountsResponse> addListeningCounts(ListeningCountsRequest listeningCountsRequest) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<ListeningCountsResponse> call = appService.addListeningCounts(listeningCountsRequest);

            call.enqueue(new Callback<ListeningCountsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ListeningCountsResponse> call,
                                       @NonNull Response<ListeningCountsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        String message = (R.string.listening_counts_add_failed) + response.message();
                        emitter.onError(new Exception(message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ListeningCountsResponse> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<TopListeningCounts> getTopMostHeardSongs() {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<TopListeningCounts> call = appService.getTopMostHeardSongs();

            call.enqueue(new Callback<TopListeningCounts>() {
                @Override
                public void onResponse(@NonNull Call<TopListeningCounts> call,
                                       @NonNull Response<TopListeningCounts> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<TopListeningCounts> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}
