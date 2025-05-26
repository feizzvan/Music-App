package com.example.musicapp.utils;

import static com.example.musicapp.utils.AppUtils.DefaultPlaylistName.DEFAULT;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.model.PlayingSong;
import com.example.musicapp.data.model.RecentSong;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.recent.RecentSongRepository;
import com.example.musicapp.data.repository.song.SongRepository;
import com.example.musicapp.data.repository.song.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public final class SharedDataUtils {
    private static final Map<String, Playlist> mPlaylistMap = new HashMap<>();
    private static final MutableLiveData<Playlist> mPlaylistLiveData = new MutableLiveData<>();
    private static final MutableLiveData<PlayingSong> mPlayingSongLiveData = new MutableLiveData<>();
    private static final PlayingSong mPlayingSong = new PlayingSong();
    private static String mPlaylistName;
    private static final MutableLiveData<Integer> mIndexToPlay = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> isSongLoaded = new MutableLiveData<>();
    private static final MutableLiveData<List<Integer>> mFavoriteSongIdsLiveData = new MutableLiveData<>(new ArrayList<>());

    static {
        initPlaylists();
    }

    private static void initPlaylists() {
        for (AppUtils.DefaultPlaylistName playlistName : AppUtils.DefaultPlaylistName.values()) {
            Playlist playlist = new Playlist(-1, playlistName.getValue());
            mPlaylistMap.put(playlistName.getValue(), playlist);
        }
    }

    public static Flowable<List<Song>> loadRecentSongs(RecentSongRepository recentSongRepository) {
        return recentSongRepository.getAllRecentSongs().map(ArrayList::new);
    }

    public static Completable saveRecentSong(Song song, RecentSongRepository recentSongRepository) {
        if (song == null) {
            return null;
        }
        RecentSong recentSong = new RecentSong.Builder(song).build();
        return recentSongRepository.insertRecentSong(recentSong);
    }

//    public static Completable updateSongInDB(Song song, SongRepository repository) {
//        return repository.updateSong(song);
//    }

    public static LiveData<List<Integer>> getFavoriteSongIdsLiveData() {
        return mFavoriteSongIdsLiveData;
    }

    public static void setFavoriteSongIds(List<Integer> ids) {
        if (ids == null) ids = new ArrayList<>();
        mFavoriteSongIdsLiveData.setValue(new ArrayList<>(ids));
    }

    public static void addFavoriteId(int songId) {
        List<Integer> ids = mFavoriteSongIdsLiveData.getValue();
        if (ids == null) ids = new ArrayList<>();
        if (!ids.contains(songId)) {
            ids.add(songId);
            mFavoriteSongIdsLiveData.setValue(new ArrayList<>(ids));
        }
    }

    public static void removeFavoriteId(int songId) {
        List<Integer> ids = mFavoriteSongIdsLiveData.getValue();
        if (ids != null && ids.contains(songId)) {
            ids.remove(Integer.valueOf(songId));
            mFavoriteSongIdsLiveData.setValue(new ArrayList<>(ids));
        }
    }

    public static boolean isFavorite(int songId) {
        List<Integer> ids = mFavoriteSongIdsLiveData.getValue();
        return ids != null && ids.contains(songId);
    }

    public static void setupPreviousSessionPlayingSong(String songId, String playlistName) {
        mPlaylistName = playlistName;
        Playlist playlist = getPlaylist(playlistName);
        if (playlist == null) {
            playlist = getPlaylist(DEFAULT.getValue());
        }
        if (songId != null && playlistName != null) {
            boolean buildInPlaylist = isBuildInPlaylist(playlistName);
            if (buildInPlaylist) {
                setCurrentPlaylist(playlistName);
            } else {
                setCurrentPlaylist(DEFAULT.getValue());
            }
            mPlayingSong.setPlaylist(playlist);
        }
    }

    private static boolean isBuildInPlaylist(String playlistName) {
        for (AppUtils.DefaultPlaylistName defaultPlaylistName : AppUtils.DefaultPlaylistName.values()) {
            if (defaultPlaylistName.getValue().equals(playlistName)) {
                return true;
            }
        }
        return false;
    }

    public static void setPlaylistSongs(List<Playlist> playlists) {
        if (playlists != null) {
            for (Playlist element : playlists) {
                element.updateSongs(element.getSongs());
                addPlaylist(element);
            }
        }
    }

    public static LiveData<Playlist> getCurrentPlaylist() {
        return mPlaylistLiveData;
    }

    public static String getCurrentPlaylistName() {
        return mPlaylistName;
    }

    public static void setCurrentPlaylist(String playlistName) {
        Playlist playlist = getPlaylist(playlistName);
        if (playlist != null) {
            mPlaylistName = playlistName;
            mPlaylistLiveData.setValue(playlist);
            mPlayingSong.setPlaylist(playlist);
        }
    }

    public static Playlist getPlaylist(String playlistName) {
        return mPlaylistMap.getOrDefault(playlistName, null);
    }

    public static List<Song> getPlaylistSongs(String playlistName) {
        Playlist playlist = getPlaylist(playlistName);
        if (playlist != null) {
            return playlist.getSongs();
        }
        return new ArrayList<>();
    }

    public static LiveData<PlayingSong> getPlayingSong() {
        return mPlayingSongLiveData;
    }

    public static void setPlayingSong(PlayingSong playingSong) {
        mPlayingSongLiveData.setValue(playingSong);
    }

    public static void setCurrentSIRSong(Song song) {
        if (song != null) {
            mPlayingSong.setSong(song);
            mPlayingSongLiveData.setValue(mPlayingSong);
        }
    }

    public static void setupPlaylist(List<Song> songs, String playlistName) {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        Playlist playlist = getPlaylist(playlistName);
        if (playlist != null) {
            playlist.updateSongs(songs);
            mPlaylistMap.put(playlistName, playlist);
        } else {
            playlist = new Playlist(-1, playlistName);
            playlist.updateSongs(songs);
            mPlaylistMap.put(playlistName, playlist);
        }
    }

    public static void addPlaylist(Playlist playlist) {
        if (!mPlaylistMap.containsKey(playlist.getName())) {
            mPlaylistMap.put(playlist.getName(), playlist);
        }
    }

    public static void setPlayingSong(int index) {
        if (index > -1 && mPlayingSong.getPlaylist() != null && mPlayingSong.getPlaylist().getSongs().size() > index) {
            Song song = mPlayingSong.getPlaylist().getSongs().get(index);
            mPlayingSong.setSong(song);
            mPlayingSong.setCurrentSongIndex(index);
            mPlayingSongLiveData.setValue(mPlayingSong);
        }
    }

    public static LiveData<Integer> getIndexToPlay() {
        return mIndexToPlay;
    }

    public static void setIndexToPlay(int index) {
        mIndexToPlay.setValue(index);
        setPlayingSong(index);
    }

    public static LiveData<Boolean> isSongLoaded() {
        return isSongLoaded;
    }

    public static void setSongLoaded(boolean isLoaded) {
        isSongLoaded.setValue(isLoaded);
    }
}