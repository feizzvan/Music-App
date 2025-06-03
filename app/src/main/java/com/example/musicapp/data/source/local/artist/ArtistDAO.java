package com.example.musicapp.data.source.local.artist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.artist.ArtistSongCrossRef;
import com.example.musicapp.data.model.artist.ArtistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ArtistDAO {
//    @Query("SELECT * FROM artists")
//    Flowable<List<Artist>> getAllArtists();

//    @Query("SELECT * FROM artists ORDER BY interested DESC LIMIT :limit")
//    Flowable<List<Artist>> getTopNArtists(int limit);

//    Thực hiện 2 truy vấn nguyên tử, phải hoàn tất mới có kết quả
//     lỗi 1 trong 2 truy vấn thì sẽ không có kết quả
//    @Transaction
//    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
//    Single<ArtistWithSongs> getArtistWithSongs(int artistId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertArtist(List<Artist> artists);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList);

    @Update
    Completable updateArtist(Artist artist);

//    @Delete
//    Completable deleteArtist(Artist artist);
}
