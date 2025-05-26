package com.example.musicapp.data.repository.listeningcounts;

import com.example.musicapp.data.model.listeningcounts.ListeningCountsRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsResponse;
import com.example.musicapp.data.model.listeningcounts.TopListeningCounts;
import com.example.musicapp.data.source.ListeningCountsDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ListeningCountsRepositoryImpl implements ListeningCountsRepository{
    private final ListeningCountsDataSource mRemoteDataSource;

    @Inject
    public ListeningCountsRepositoryImpl(ListeningCountsDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<ListeningCountsResponse> addListeningCounts(ListeningCountsRequest listeningCountsRequest) {
        return mRemoteDataSource.addListeningCounts(listeningCountsRequest);
    }

    @Override
    public Single<TopListeningCounts> getTopMostHeardSongs() {
        return mRemoteDataSource.getTopMostHeardSongs();
    }
}
