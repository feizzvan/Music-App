package com.example.musicapp.data.model.album;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Lấy danh sách Album
public class AlbumList {
    @SerializedName("data")
    private List<Album> albums;

    public List<Album> getAlbums() {
        return albums;
    }
}
