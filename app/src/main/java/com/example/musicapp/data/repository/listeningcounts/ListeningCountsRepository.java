package com.example.musicapp.data.repository.listeningcounts;

import com.example.musicapp.data.model.listeningcounts.ListeningCountsRequest;
import com.example.musicapp.data.model.listeningcounts.ListeningCountsResponse;
import com.example.musicapp.data.model.listeningcounts.TopListeningCounts;

import io.reactivex.rxjava3.core.Single;

public interface ListeningCountsRepository {
    Single<ListeningCountsResponse> addListeningCounts(ListeningCountsRequest listeningCountsRequest);

    Single<TopListeningCounts> getTopMostHeardSongs();

}
