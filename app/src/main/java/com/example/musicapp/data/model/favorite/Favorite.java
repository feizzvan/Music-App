package com.example.musicapp.data.model.favorite;

import androidx.media3.common.MediaItem;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Favorite {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("song_id")
    private int songId;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("songs")
    private List<Song> songs;

    private final List<MediaItem> mMediaItems = new ArrayList<>();

    public Favorite(int userId, int songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public Favorite() {
    }

    public Favorite(int id, int userId, int songId, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void updateSongs(List<Song> songs) {
        if (songs != null && !songs.isEmpty()) {
            this.songs = songs;
            updateMediaItems();
        }
    }

    public List<MediaItem> getMediaItems() {
        return mMediaItems;
    }

    private void updateMediaItems() {
        mMediaItems.clear();
        for (Song song : songs) {
            mMediaItems.add(MediaItem.fromUri(song.getFullFileUrl()));
        }
    }
}
