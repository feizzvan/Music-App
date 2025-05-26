package com.example.musicapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.musicapp.data.model.song.Song;

import java.util.Date;

@Entity(tableName = "recent_songs")
public class RecentSong extends Song {
    @ColumnInfo(name = "play_at")
    private Date mPlayAt;

    // Trả về thời gian phát gần nhất của bài hát
    public Date getPlayAt() {
        return mPlayAt;
    }

    // Thiết lập thời gian phát gần nhất của bài hát
    public void setPlayAt(Date playAt) {
        mPlayAt = playAt;
    }

    // Lớp Builder để xây dựng đối tượng RecentSong từ lớp Song
    public static class Builder {
        private static RecentSong sRecentSong;

        public Builder(Song song) {
            sRecentSong = new RecentSong();
            sRecentSong.setId(song.getId());
            sRecentSong.setTitle(song.getTitle());
//            sRecentSong.setAlbum(song.getAlbum());
            sRecentSong.setArtistId(song.getArtistId());
            sRecentSong.setArtistName(song.getArtistName() );
            sRecentSong.setFileUrl(song.getFullFileUrl());
            sRecentSong.setImageUrl(song.getImageUrl());
            sRecentSong.setDuration(song.getDuration());
//            sRecentSong.setFavorite(song.isFavorite());
////            sRecentSong.setCounter(song.getCounter());
////            sRecentSong.setReplay(song.getReplay());
            sRecentSong.mPlayAt = new Date();
        }

        public Builder setPlayAt(Date playAt) {
            sRecentSong.mPlayAt = playAt;
            return this; // Trả về chính đối tượng Builder để hỗ trợ chaining method (gọi liên tiếp các phương thức)
        }

        // Xây dựng đối tượng RecentSong
        public RecentSong build() {
            return sRecentSong; // Trả về đối tượng RecentSong đã được thiết lập
        }
    }
}
