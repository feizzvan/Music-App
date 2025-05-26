package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.searching.SearchingRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final SearchingRepository mSearchingRepository;

    @Inject
    public MainViewModel(SearchingRepository searchingRepository) {
        mSearchingRepository = searchingRepository;
    }

    public Flowable<List<Song>> loadHistorySearchedSongs() {
        return mSearchingRepository.getHistorySearchedSongs().map(ArrayList::new);

    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SearchingRepository mSearchingRepository;

        @Inject
        public Factory(SearchingRepository searchingRepository) {
            mSearchingRepository = searchingRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MainViewModel.class)) {
                return (T) new MainViewModel(mSearchingRepository);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}
