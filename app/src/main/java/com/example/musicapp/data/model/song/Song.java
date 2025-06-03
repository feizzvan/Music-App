package com.example.musicapp.data.model.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.musicapp.utils.AppConfig;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "songs")
public class Song {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    private int mId;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("artistId")
    @ColumnInfo(name = "artist_id")
    private int mArtistId;

    @SerializedName("artistName")
    @ColumnInfo(name = "artist_name")
    private String mArtistName;

    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    private int mDuration;

    @SerializedName("fileUrl")
    @ColumnInfo(name = "file_url")
    private String mFileUrl;

    @SerializedName("imageUrl")
    @ColumnInfo(name = "image_url")
    private String mImageUrl;

    @SerializedName("genreId")
    @Ignore
    private int genreId;

    @SerializedName("genreName")
    @Ignore
    private String genreName;

    @SerializedName("listenCount")
    @Ignore
    private long listenCount;

    @SerializedName("favoriteCount")
    @Ignore
    private long favoriteCount;

    @SerializedName("createdAt")
    @Ignore
    private Date createdAt;

    public Song() {
    }

    @Ignore
    public Song(int Id) {
        setId(Id);
    }

    public Song(int Id, String Title, int artistId, int Duration, String fileUrl, String imageUrl) {
        setId(Id);
        setTitle(Title);
        setArtistId(artistId);
        setFileUrl(fileUrl);
        setImageUrl(imageUrl);
        setDuration(Duration);
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

    public int getArtistId() {
        return mArtistId;
    }

    public void setArtistId(int artist) {
        this.mArtistId = artist;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        this.mArtistName = artistName;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String source) {
        this.mFileUrl = source;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String image) {
        this.mImageUrl = image;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public String getFullFileUrl() {
        //Base URL của server
//        String baseUrl = "http://192.168.0.101:8080";
        String baseUrl = AppConfig.BASE_URL;
        if (mFileUrl != null && !mFileUrl.startsWith("http")) {
            return baseUrl + mFileUrl;
        }
        return mFileUrl;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public long getListenCount() {
        return listenCount;
    }

    public void setListenCount(long listenCount) {
        this.listenCount = listenCount;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(mId, song.mId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }
}
