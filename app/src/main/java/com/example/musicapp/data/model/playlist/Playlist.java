package com.example.musicapp.data.model.playlist;

import androidx.media3.common.MediaItem;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "playlists")
public class Playlist {
    @Ignore

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "name")
    @SerializedName("title")
    private String mName;

    @ColumnInfo(name = "user_id")
    @SerializedName("userId")
    private int userId;

    @ColumnInfo(name = "created_at")
    @SerializedName("createdAt")
    private Date mCreatedAt;

    @Ignore
    @ColumnInfo(name = "songs")
    @SerializedName("songs")
    private List<Song> mSongs;

    @Ignore
    private final List<MediaItem> mMediaItems = new ArrayList<>();

    public Playlist() {

    }

    @Ignore
    public Playlist(String name, int userId, List<Song> songs) {
        setName(name);
        setUserId(userId);
        updateSongs(songs);
    }

    @Ignore
    public Playlist(int id, String name) {
        this(id, name, null, null);
    }

    @Ignore
    public Playlist(int id, String name, Date createdAt, List<Song> songs) {
        setId(id);
        setName(name);
        setCreatedAt(createdAt);
        updateSongs(songs);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        if (id > 0) {
            mId = id;
        }
//        else {
//            mId = sNextId++;
//        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void updateSongs(List<Song> songs) {
        if (songs != null && !songs.isEmpty()) {
            mSongs = songs;
            updateMediaItems();
        }
    }

    public List<MediaItem> getMediaItems() {
        return mMediaItems;
    }

    private void updateMediaItems() {
        mMediaItems.clear();
        for (Song song : mSongs) {
            mMediaItems.add(MediaItem.fromUri(song.getFullFileUrl()));
        }
    }
}
