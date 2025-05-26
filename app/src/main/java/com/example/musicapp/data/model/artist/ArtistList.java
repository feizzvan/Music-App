package com.example.musicapp.data.model.artist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ArtistList {
    @SerializedName("data")
    public List<Artist> artists = new ArrayList<>();

    public ArtistList() {
    }

    public ArtistList(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Artist> getArtists() {
        return artists;
    }
}
