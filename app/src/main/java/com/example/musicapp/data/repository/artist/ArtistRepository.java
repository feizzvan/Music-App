package com.example.musicapp.data.repository.artist;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.artist.ArtistList;
import com.example.musicapp.data.model.artist.ArtistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

//định nghĩa các phương thức liên quan đến việc truy xuất dữ liệu nghệ sĩ (Artist).
public interface ArtistRepository {
    interface Local {
//        Flowable<List<Artist>> getTopNArtists(int limit);

        Completable insertArtist(List<Artist> artists);

        Completable updateArtist(Artist artist);

//        Completable deleteArtist(Artist artist);
    }

    interface Remote {
        Single<ArtistList> getAllArtists();

        Single<ArtistWithSongs> getArtistWithSongs(int artistId);
    }
}
