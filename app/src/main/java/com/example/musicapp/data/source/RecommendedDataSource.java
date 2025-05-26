package com.example.musicapp.data.source;

import com.example.musicapp.data.model.recommended.TopRecommended;

import io.reactivex.rxjava3.core.Single;

public interface RecommendedDataSource {
    Single<TopRecommended> getRecommendationSongs(int userId);
}
