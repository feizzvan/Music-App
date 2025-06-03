package com.example.musicapp.ui.discovery.artist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.artist.ArtistWithSongs;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl;
import com.example.musicapp.data.repository.song.SongRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ArtistViewModel extends ViewModel {
    private final ArtistRepositoryImpl mArtistRepository;
    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Artist>> mArtists = new MutableLiveData<>();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Artist>> mLocalArtists = new MutableLiveData<>();

    @Inject
    public ArtistViewModel(ArtistRepositoryImpl artistRepository, SongRepositoryImpl songRepository) {
        mArtistRepository = artistRepository;
        mSongRepository = songRepository;
        loadArtists();
    }

    public void setArtist(List<Artist> artists) {
        mArtists.postValue(artists);
    }

    public LiveData<List<Artist>> getArtist() {
        return mArtists;
    }

    private void loadArtists() {
        mDisposable.add(mArtistRepository.getAllArtists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists -> setArtist(artists.getArtists()),
                        throwable -> {
                        })
        );
    }

    public void setLocalArtists(List<Artist> artists) {
        mLocalArtists.postValue(artists);
    }

    public LiveData<List<Artist>> getLocalArtists() {
        return mLocalArtists;
    }

    public Completable saveArtistToLocalDB(List<Artist> artists) {
        return mArtistRepository.insertArtist(artists);
    }

    public Single<List<Song>> loadAllSongs() {
        return mSongRepository.getSongs();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ArtistRepositoryImpl mArtistRepository;
        private final SongRepositoryImpl mSongRepository;

        @Inject
        public Factory(ArtistRepositoryImpl artistRepository, SongRepositoryImpl songRepository) {
            mArtistRepository = artistRepository;
            mSongRepository = songRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ArtistViewModel.class)) {
                return (T) new ArtistViewModel(mArtistRepository, mSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class" + modelClass.getName());
        }
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}