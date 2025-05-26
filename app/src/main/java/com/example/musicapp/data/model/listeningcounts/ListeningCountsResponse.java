package com.example.musicapp.data.model.listeningcounts;

import com.example.musicapp.data.model.song.Song;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ListeningCountsResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("song")
    private Song song;

    @SerializedName("count")
    private int count;

    @SerializedName("listenTime")
    private Date listenTime;

    public ListeningCountsResponse() {
    }

    public ListeningCountsResponse(int id, Song song, int count, Date listenTime) {
        this.id = id;
        this.song = song;
        this.count = count;
        this.listenTime = listenTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getListenTime() {
        return listenTime;
    }

    public void setListenTime(Date listenTime) {
        this.listenTime = listenTime;
    }
}
