package com.example.musicapp.data.source;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.artist.ArtistList;
import com.example.musicapp.data.model.artist.ArtistSongCrossRef;
import com.example.musicapp.data.model.artist.ArtistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

//Định nghĩa các nguồn cho Artist
public interface ArtistDataSource {
    interface Local {
//        Flowable<List<Artist>> getAllArtists();
//
//        Single<ArtistWithSongs> getArtistWithSongs(int artistId);

        Completable insertArtist(List<Artist> artists);

//        Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList);

        Completable updateArtist(Artist artist);

//        Completable deleteArtist(Artist artist);
    }

    interface Remote {
        Single<ArtistList> getAllArtists();

        Single<ArtistWithSongs> getArtistWithSongs(int artistId);
    }
}
