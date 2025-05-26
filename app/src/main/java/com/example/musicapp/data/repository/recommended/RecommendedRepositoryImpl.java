package com.example.musicapp.data.repository.recommended;

import com.example.musicapp.data.model.recommended.TopRecommended;
import com.example.musicapp.data.source.RecommendedDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RecommendedRepositoryImpl implements RecommendedRepository {
    private final RecommendedDataSource mRecommendedDataSource;

    @Inject
    public RecommendedRepositoryImpl(RecommendedDataSource recommendedDataSource) {
        mRecommendedDataSource = recommendedDataSource;
    }

    @Override
    public Single<TopRecommended> getRecommendationSongs(int userId) {
        return mRecommendedDataSource.getRecommendationSongs(userId);
    }
}
