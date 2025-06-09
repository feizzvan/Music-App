package com.example.musicapp.data.model.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.musicapp.data.model.song.Song;

import java.util.Date;

@Entity(tableName = "history_searched_songs")
public class HistorySearchedSong extends Song {
    @ColumnInfo(name = "selected_at")
    private Date mSelectedAt;

    public Date getSelectedAt() {
        return mSelectedAt;
    }

    public void setSelectedAt(Date selectedAt) {
        mSelectedAt = selectedAt;
    }

    public static class Builder {
        private final HistorySearchedSong mInstance = new HistorySearchedSong();
        private final Song mSong;

        public Builder(Song song) {
            mSong = song;
        }

        public HistorySearchedSong build() {
            mInstance.setId(mSong.getId());
            mInstance.setTitle(mSong.getTitle());
            mInstance.setArtistId(mSong.getArtistId());
            mInstance.setArtistName(mSong.getArtistName());
            mInstance.setDuration(mSong.getDuration());
            mInstance.setImageUrl(mSong.getImageUrl());
            mInstance.setGenreName(mSong.getGenreName());
            mInstance.setListenCount(mSong.getListenCount());
            mInstance.setFavoriteCount(mSong.getFavoriteCount());
//            mInstance.setCounter(mSong.getCounter());
            mInstance.setFileUrl(mSong.getFullFileUrl());
            mInstance.setSelectedAt(new Date());
            return mInstance;
        }
    }
}
