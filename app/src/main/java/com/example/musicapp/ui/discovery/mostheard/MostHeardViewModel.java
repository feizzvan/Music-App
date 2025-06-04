package com.example.musicapp.ui.discovery.mostheard;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.listeningcounts.TopListeningCounts;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.listeningcounts.ListeningCountsRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class MostHeardViewModel extends ViewModel {
    private final ListeningCountsRepositoryImpl mListeningCountsRepository;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    @Inject
    public MostHeardViewModel(ListeningCountsRepositoryImpl listeningCountsRepository) {
        mListeningCountsRepository = listeningCountsRepository;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
    }

    public Single<List<Song>> loadTopMostHeardSong() {
        return mListeningCountsRepository.getTopMostHeardSongs().map(TopListeningCounts::getSongs);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ListeningCountsRepositoryImpl mListeningCountsRepository;

        @Inject
        public Factory(ListeningCountsRepositoryImpl listeningCountsRepository) {
            mListeningCountsRepository = listeningCountsRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MostHeardViewModel.class)) {
                return (T) new MostHeardViewModel(mListeningCountsRepository);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}