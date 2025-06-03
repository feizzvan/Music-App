package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.data.model.history.Search;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.source.SearchingDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteSearchingDataSourceImpl implements SearchingDataSource.Remote {
    @Inject
    public RemoteSearchingDataSourceImpl() {
    }

    @Override
    public Single<List<Song>> search(String key) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<Search> call = appService.search(key);

            call.enqueue(new Callback<Search>() {
                @Override
                public void onResponse(@NonNull Call<Search> call,
                                       @NonNull Response<Search> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body().getSongs());
                    } else {
                        emitter.onError(new Exception("Search failed: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Search> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}
