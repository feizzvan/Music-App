package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.data.model.artist.ArtistList;
import com.example.musicapp.data.model.artist.ArtistWithSongs;
import com.example.musicapp.data.source.ArtistDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteArtistDataSource implements ArtistDataSource.Remote {
    @Inject
    public RemoteArtistDataSource() {
    }

    @Override
    public Single<ArtistList> getAllArtists() {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<ArtistList> call = appService.getArtists();

            call.enqueue(new Callback<ArtistList>() {
                @Override
                public void onResponse(@NonNull Call<ArtistList> call,
                                       @NonNull Response<ArtistList> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ArtistList> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<ArtistWithSongs> call = appService.getSongsByArtist(artistId);

            call.enqueue(new Callback<ArtistWithSongs>() {
                @Override
                public void onResponse(@NonNull Call<ArtistWithSongs> call,
                                       @NonNull Response<ArtistWithSongs> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ArtistWithSongs> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}
