package com.example.musicapp.data.model.playlist;

import com.example.musicapp.utils.TokenManager;

import java.util.List;

public class CreatePlaylist {
    private String title;
    private Integer userId;
    private List<Integer> songIds;

    public CreatePlaylist() {
    }

    public CreatePlaylist(String title, Integer userId, List<Integer> songIds) {
        this.title = title;
        this.userId = userId;
        this.songIds = songIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds) {
        this.songIds = songIds;
    }
}
