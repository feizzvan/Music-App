package com.example.musicapp.data.model.artist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "artist_song_cross_ref",
        primaryKeys = {"artist_id", "song_id"},
        indices = {
                @Index("artist_id"),
                @Index("song_id")
        }
)
public class ArtistSongCrossRef {
    @ColumnInfo(name = "artist_id")
    public int artistId;

    @ColumnInfo(name = "song_id")
    @NonNull
    public String songId = "";

    public ArtistSongCrossRef() {

    }

    public ArtistSongCrossRef(int artistId, @NonNull String songId) {
        this.artistId = artistId;
        this.songId = songId;
    }
}
