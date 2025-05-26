package com.example.musicapp.data.model.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "history_searched_keys",
        indices = {@Index(value = {"key"}, unique = true)}
)
public class HistorySearchedKey {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "key")
    private String mKey;

    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;

    public HistorySearchedKey() {
    }

    public HistorySearchedKey(int id, String key, Date createdAt) {
        mId = id;
        mKey = key;
        mCreatedAt = createdAt;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }
}
