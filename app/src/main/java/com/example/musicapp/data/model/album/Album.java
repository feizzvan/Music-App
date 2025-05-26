package com.example.musicapp.data.model.album;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Lấy danh sách Album
@Entity(tableName = "album")
public class Album {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "album_id")
    private int mId;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("coverImageUrl")
    @ColumnInfo(name = "cover_image_url")
    private String mCoverImageUrl;

    @SerializedName("createdAt")
    @Ignore
    @ColumnInfo(name = "created_at")
    private String mCreatedAt;

    @SerializedName("genreName")
    @Ignore
    @ColumnInfo(name = "genre_name")
    private String mGenreName;

    @SerializedName("songs")
    @Ignore
    private final List<Song> mSongs = new ArrayList<>();

    public Album() {
    }

    @Ignore
    public Album(int id, String title, String coverImageUrl) {
        mId = id;
        mTitle = title;
        mCoverImageUrl = coverImageUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getCoverImageUrl() {
        return mCoverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.mCoverImageUrl = coverImageUrl;
    }

    public void setSongs(List<Song> songs) {
        if (songs != null && !songs.isEmpty()) {
            mSongs.clear();
            mSongs.addAll(songs);
        }
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return mId == album.mId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId);
    }
}
