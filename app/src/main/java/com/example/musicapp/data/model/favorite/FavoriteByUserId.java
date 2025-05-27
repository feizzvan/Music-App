package com.example.musicapp.data.model.favorite;

import com.google.gson.annotations.SerializedName;

public class FavoriteByUserId {
    @SerializedName("data")
    private Favorite data;


    public Favorite getData() {
        return data;
    }

    public void setData(Favorite data) {
        this.data = data;
    }
}
