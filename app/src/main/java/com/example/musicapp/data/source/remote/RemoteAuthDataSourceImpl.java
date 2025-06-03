package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.UpdatePasswordRequest;
import com.example.musicapp.data.source.AuthDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteAuthDataSourceImpl implements AuthDataSource {
    @Inject
    public RemoteAuthDataSourceImpl() {
    }

    @Override
    public Single<AuthenticationResponse> updatePassword(String token, UpdatePasswordRequest updatePasswordRequest) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<AuthenticationResponse> call = appService.updatePassword(token, updatePasswordRequest);

            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(@NonNull Call<AuthenticationResponse> call,
                                       @NonNull Response<AuthenticationResponse> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<AuthenticationResponse> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<AuthenticationResponse> logout(LogoutRequest logoutRequest) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<AuthenticationResponse> call = appService.logout(logoutRequest);

            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(@NonNull Call<AuthenticationResponse> call,
                                       @NonNull Response<AuthenticationResponse> response) {
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<AuthenticationResponse> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}
