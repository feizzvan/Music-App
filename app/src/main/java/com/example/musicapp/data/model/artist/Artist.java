package com.example.musicapp.data.model.artist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@SuppressWarnings("unused")
@Entity(tableName = "artists")
public class Artist {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "artist_id")
    private int mId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("imageUrl")
    @ColumnInfo(name = "image_url")
    private String mAvatar;

    @SerializedName("listenerCount")
    @ColumnInfo(name = "listener_count")
    private long mListenerCount;

    @SerializedName("numberOfSongs")
    @ColumnInfo(name = "number_of_song")
    private int mSongCount;

    @SerializedName("createdAt")
    @ColumnInfo(name = "created_at")
    private String mCreatedAt;


    public Artist() {

    }

    @Ignore
    public Artist(int mId, String mName, String mAvatar, long mListenerCount, int mSongCount, String mCreatedAt) {
        this.mId = mId;
        this.mName = mName;
        this.mAvatar = mAvatar;
        this.mListenerCount = mListenerCount;
        this.mSongCount = mSongCount;
        this.mCreatedAt = mCreatedAt;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public long getListenerCount() {
        return mListenerCount;
    }

    public void setListenerCount(long listenerCount) {
        mListenerCount = listenerCount;
    }

    public int getSongCount() {
        return mSongCount;
    }

    public void setSongCount(int songCount) {
        mSongCount = songCount;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return mId == artist.mId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId);
    }
}
