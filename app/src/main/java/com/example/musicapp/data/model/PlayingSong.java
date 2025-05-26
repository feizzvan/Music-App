package com.example.musicapp.data.model;

import androidx.media3.common.MediaItem;

import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;

public class PlayingSong {
    private Song mSong;
    private MediaItem mMediaItem;
    private Playlist mPlaylist;
    private int mCurrentPosition;
    private int mCurrentSongIndex;
    private int mNextSongIndex;

    public PlayingSong() {
        this(null, null);
    }

    public PlayingSong(Song song, Playlist playlist) {
        this(song, playlist, -1);
    }

    public PlayingSong(Song song, Playlist playlist, int currentIndex) {
        setSong(song);
        setPlaylist(playlist);
        setCurrentSongIndex(currentIndex);
    }

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        this.mSong = song;
        if (song != null) {
            setMediaItem(MediaItem.fromUri(song.getFullFileUrl()));
        }
    }

    public MediaItem getMediaItem() {
        return mMediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        this.mMediaItem = mediaItem;
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(Playlist mPlaylist) {
        this.mPlaylist = mPlaylist;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public int getCurrentSongIndex() {
        return mCurrentSongIndex;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        this.mCurrentSongIndex = currentSongIndex;
    }

    public int getNextSongIndex() {
        return mNextSongIndex;
    }

    public void setNextSongIndex(int nextSongIndex) {
        this.mNextSongIndex = nextSongIndex;
    }
}
