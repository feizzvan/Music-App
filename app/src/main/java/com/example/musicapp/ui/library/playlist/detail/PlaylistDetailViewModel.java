        package com.example.musicapp.ui.library.playlist.detail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.playlist.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class PlaylistDetailViewModel extends ViewModel {
    private final MutableLiveData<Playlist> mPlaylist = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();
    private Playlist mPlaylistForPlayback;
    private final PlaylistRepository.Remote mPlaylistRepository;

    @Inject
    public PlaylistDetailViewModel(PlaylistRepository.Remote playlistRepository) {
        mPlaylistRepository = playlistRepository;
    }

    public void loadPlaylistById(int playlistId) {
        mPlaylistRepository.loadPlaylistById(playlistId, new Callback<PlaylistById>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Playlist playlist = response.body().getData();
                    mPlaylist.postValue(playlist);
                    List<Song> songs = playlist.getSongs() != null ? playlist.getSongs() : new ArrayList<>();
                    mSongs.postValue(songs);
                    // Tạo playlist cho phát nhạc
                    mPlaylistForPlayback = new Playlist(playlist.getId(), playlist.getName());
                    mPlaylistForPlayback.updateSongs(songs);
                    Log.d("PlaylistDetailViewModel", "Playlist fetched by ID: " + playlist.getName() + ", Song count: " + songs.size());
                } else {
                    mPlaylist.postValue(null);
                    mSongs.postValue(new ArrayList<>());
                    mPlaylistForPlayback = null;
                    Log.e("PlaylistDetailViewModel", "Failed to fetch playlist: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable throwable) {
                mPlaylist.postValue(null);
                mSongs.postValue(new ArrayList<>());
                mPlaylistForPlayback = null;
                Log.e("PlaylistDetailViewModel", "Error fetching playlist: " + throwable.getMessage());
            }
        });
    }

    public void setPlaylist(int id, String name, Integer userId) {
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(name);
        playlist.setUserId(userId);
        mPlaylist.setValue(playlist);
        mSongs.setValue(new ArrayList<>());
        mPlaylistForPlayback = new Playlist(id, name);
        mPlaylistForPlayback.updateSongs(new ArrayList<>());
    }

    public void createPlaylist(String playlistName) {
        mPlaylistForPlayback = new Playlist(-1, playlistName);
        List<Song> songs = mSongs.getValue();
        mPlaylistForPlayback.updateSongs(Objects.requireNonNullElseGet(songs, ArrayList::new));
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
        if (mPlaylistForPlayback != null) {
            mPlaylistForPlayback.updateSongs(songs != null ? songs : new ArrayList<>());
        }
    }

    public LiveData<Playlist> getPlaylist() {
        return mPlaylist;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public Playlist getPlaylistForPlayback() {
        return mPlaylistForPlayback;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final PlaylistRepository.Remote mPlaylistRepository;

        @Inject
        public Factory(PlaylistRepository.Remote playlistRepository) {
            mPlaylistRepository = playlistRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlaylistDetailViewModel.class)) {
                return (T) new PlaylistDetailViewModel(mPlaylistRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
