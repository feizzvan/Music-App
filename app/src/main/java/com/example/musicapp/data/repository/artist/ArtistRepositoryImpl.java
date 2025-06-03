package com.example.musicapp.data.repository.artist;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.artist.ArtistList;
import com.example.musicapp.data.model.artist.ArtistWithSongs;
import com.example.musicapp.data.source.ArtistDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ArtistRepositoryImpl implements ArtistRepository.Local, ArtistRepository.Remote {
    private final ArtistDataSource.Local mLocalArtistDataSource;
    private final ArtistDataSource.Remote mRemoteArtistDataSource;

    @Inject
    public ArtistRepositoryImpl(ArtistDataSource.Local localArtistDataSource,
                                ArtistDataSource.Remote remoteArtistDataSource) {
        mLocalArtistDataSource = localArtistDataSource;
        mRemoteArtistDataSource = remoteArtistDataSource;
    }

    @Override
    public Single<ArtistList> getAllArtists() {
        return mRemoteArtistDataSource.getAllArtists();
    }

    @Override
    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
        return mRemoteArtistDataSource.getArtistWithSongs(artistId);
    }

    //    @Override
//    public void loadArtists(Callback<ArtistList> callback) {
//        mRemoteArtistDataSource.loadArtists(callback);
//    }
//
//    @Override
//    public Flowable<List<Artist>> getTopNArtists(int limit) {
//        return mLocalArtistDataSource.getTopNArtists(limit);
//    }
//
//    @Override
//    public Flowable<List<Artist>> getAllArtists() {
//        return mLocalArtistDataSource.getAllArtists();
//    }
//
//    @Override
//    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
//        return mLocalArtistDataSource.getArtistWithSongs(artistId);
//    }
//
    @Override
    public Completable insertArtist(List<Artist> artists) {
        return mLocalArtistDataSource.insertArtist(artists);
    }
//
//    @Override
//    public Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList) {
//        return mLocalArtistDataSource.insertArtistSongCrossRef(artistSongCrossRefList);
//    }
//
    @Override
    public Completable updateArtist(Artist artist) {
        return mLocalArtistDataSource.updateArtist(artist);
    }
//
//    @Override
//    public Completable deleteArtist(Artist artist) {
//        return mLocalArtistDataSource.deleteArtist(artist);
//    }
}
