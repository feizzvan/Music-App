package com.example.musicapp.data.source.remote;

import com.example.musicapp.data.model.album.AlbumById;
import com.example.musicapp.data.model.album.AlbumList;
import com.example.musicapp.data.model.artist.ArtistList;
import com.example.musicapp.data.model.artist.ArtistWithSongs;
import com.example.musicapp.data.model.auth.ForgotPasswordRequest;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.ResetPassword;
import com.example.musicapp.data.model.favorite.Favorite;
import com.example.musicapp.data.model.favorite.FavoriteByUserId;
import com.example.musicapp.data.model.favorite.FavoriteRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsResponse;
import com.example.musicapp.data.model.listeningcounts.TopListeningCounts;
import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.recommended.TopRecommended;
import com.example.musicapp.data.model.song.SongList;
import com.example.musicapp.data.model.auth.LoginRequest;
import com.example.musicapp.data.model.auth.RegisterRequest;
import com.example.musicapp.data.model.auth.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// `AppService` là một interface dùng để định nghĩa các yêu cầu API tới server từ xa bằng cách sử dụng Retrofit.
// Ở đây, nó định nghĩa một phương thức để lấy danh sách album từ một URL cụ thể.

public interface AppService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth/sign-in")
    Call<AuthenticationResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/v1/auth/register")
    Call<AuthenticationResponse> register(@Body RegisterRequest registerRequest);

    @POST("/api/v1/auth/forgot-password")
    Call<AuthenticationResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("/api/v1/auth/reset-password")
    Call<AuthenticationResponse> resetPassword(@Body ResetPassword resetPassword);

    @POST("/api/v1/auth/log-out")
    Call<AuthenticationResponse> logout(@Body LogoutRequest logoutRequest);

    @GET("/api/v1/songs/get-all")
    Call<SongList> getSongs();

    @GET("/api/v1/albums/get-all")
    Call<AlbumList> getAlbums();

    @GET("/api/v1/albums/get-by-id/{id}")
    Call<AlbumById> getAlbumById(@Path("id") int id);

    @POST("/api/v1/playlists/create")
    Call<PlaylistById> createPlaylist(@Body CreatePlaylist createPlaylist);

    @PUT("/api/v1/playlists/update/{id}")
    Call<PlaylistById> updatePlayList(@Path("id") int id, @Body CreatePlaylist createPlaylist);

    @HTTP(method = "DELETE", path = "/api/v1/playlists/delete/{id}", hasBody = true)
    Call<PlaylistById> deletePlaylist(@Path("id") int id);

    @GET("/api/v1/playlists/user/{userId}")
    Call<PlaylistByUserId> getPlaylistByUserId(@Path("userId") int userId);

    @GET("/api/v1/playlists/get-by-id/{id}")
    Call<PlaylistById> getPlaylistById(@Path("id") int id);

    @POST("/api/v1/favorites/add")
    Call<Favorite> addFavorite(@Body FavoriteRequest favoriteRequest);

    @HTTP(method = "DELETE", path = "/api/v1/favorites/remove", hasBody = true)
    Call<Favorite> removeFavorite(@Body FavoriteRequest favoriteRequest);

    @GET("/api/v1/favorites/get-by-user-id/{userId}")
    Call<FavoriteByUserId> getFavoriteByUserId(@Path("userId") int userId);

    @POST("/api/v1/listening-counts")
    Call<ListeningCountsResponse> addListeningCounts(@Body ListeningCountsRequest listeningCountsRequest);

    @GET("/api/v1/listening-counts/top")
    Call<TopListeningCounts> getTopMostHeardSongs();

    @GET("/api/v1/recommendations/user/{userId}")
    Call<TopRecommended> getRecommendations(@Path("userId") int userId);

    @GET("/api/v1/artists/get-all")
    Call<ArtistList> getArtists();

    @GET("/api/v1/songs/by-artist/{artistId}")
    Call<ArtistWithSongs> getSongsByArtist(@Path("artistId") int artistId);
}
