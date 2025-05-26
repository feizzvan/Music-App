package com.example.musicapp.ui.home.album.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.data.model.album.AlbumById;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.album.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class DetailAlbumViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();
    private final MutableLiveData<Album> mAlbum = new MutableLiveData<>();
    private Playlist mPlaylist;
    private final AlbumRepository mAlbumRepository;

    @Inject
    public DetailAlbumViewModel(AlbumRepository albumRepository) {
        mAlbumRepository = albumRepository;
    }

    public void loadAlbumById(int albumId) {
        mAlbumRepository.loadAlbumById(albumId, new Callback<AlbumById>() {
            @Override
            public void onResponse(@NonNull Call<AlbumById> call, @NonNull Response<AlbumById> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Album album = response.body().getAlbum();
                    mAlbum.postValue(album);

                    //Album có sẵn danh sách bài hát
                    mPlaylist = new Playlist(album.getId(), album.getTitle());
                    List<Song> songs = album.getSongs();
                    mSongs.postValue(songs);
                    mPlaylist.updateSongs(songs);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumById> call, @NonNull Throwable throwable) {
                mSongs.postValue(new ArrayList<>());
            }
        });
    }

    public void createPlaylist(String playlistName) {
        mPlaylist = new Playlist(-1, playlistName);
        List<Song> songs = mSongs.getValue();
        mPlaylist.updateSongs(Objects.requireNonNullElseGet(songs, ArrayList::new));
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setAlbum(int id, String title, String coverImageUrl) {
        Album album = new Album(id, title, coverImageUrl);
        mAlbum.setValue(album);
    }

    public LiveData<Album> getAlbum() {
        return mAlbum;
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final AlbumRepository mAlbumRepository;

        @Inject
        public Factory(AlbumRepository albumRepository) {
            mAlbumRepository = albumRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DetailAlbumViewModel.class)) {
                return (T) new DetailAlbumViewModel(mAlbumRepository);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}
