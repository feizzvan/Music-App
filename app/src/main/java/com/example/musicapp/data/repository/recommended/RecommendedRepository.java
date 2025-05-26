package com.example.musicapp.data.repository.recommended;

import com.example.musicapp.data.model.recommended.TopRecommended;

import io.reactivex.rxjava3.core.Single;

public interface RecommendedRepository {
    Single<TopRecommended> getRecommendationSongs(int userId);
}
