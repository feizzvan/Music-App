package com.example.musicapp.ui.home.recommended;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.recommended.RecommendedRepositoryImpl;
import com.example.musicapp.utils.TokenManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecommendedSongViewModel extends ViewModel {
    //    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Song>> mSongList = new MutableLiveData<>();
    private RecommendedRepositoryImpl mRecommendedRepository;
    private TokenManager mTokenManager;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public RecommendedSongViewModel(RecommendedRepositoryImpl recommendedRepository, TokenManager tokenManager) {
//        mSongRepository = songRepository;
//        loadRecommendedSongs();
        mRecommendedRepository = recommendedRepository;
        mTokenManager = tokenManager;
    }

    public void loadRecommendedSongs() {
        mDisposable.add(mRecommendedRepository.getRecommendationSongs(mTokenManager.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> setSong(songs.getSongs()),
                        throwable -> {
                        }
                )
        );
    }

//    Completable saveSongToDB(List<Song> songs){
//        if(songs == null){
//            return Completable.complete();
//        }
//        Song[] songArray = songs.toArray(new Song[0]);
//        return mSongRepository.saveSongs(songArray);
//    }

    public void setSong(List<Song> songs) {
        if (songs != null) {
            mSongList.postValue(songs);
        }
    }

    public LiveData<List<Song>> getSongList() {
        return mSongList;
    }

    public static class Factory implements ViewModelProvider.Factory {
        //        private final SongRepositoryImpl mSongRepository;
        private final RecommendedRepositoryImpl mRecommendedRepository;
        private final TokenManager mTokenManager;

        @Inject
        public Factory(RecommendedRepositoryImpl recommendedRepository, TokenManager tokenManager) {
//            mSongRepository = songRepository;
            mRecommendedRepository = recommendedRepository;
            mTokenManager = tokenManager;
        }

        @NonNull
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RecommendedSongViewModel.class)) {
                return (T) new RecommendedSongViewModel(mRecommendedRepository, mTokenManager);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}
