package com.example.contact_client;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoCut {
    //视频片段，数据库的成员

    //自动生成id
    @PrimaryKey(autoGenerate = true)
    private int id;
    //主要属性
    @ColumnInfo(name = "isCut")
    private boolean isCut;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "urlString")
    private String urlString;
    @ColumnInfo(name = "thumbnailPath")
    private String thumbnailPath;

    public VideoCut(boolean isCut, String name, String description, String urlString, String thumbnailPath) {
        this.isCut = isCut;
        this.name = name;
        this.description = description;
        this.urlString = urlString;
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + description + " " + urlString + " " + thumbnailPath;
    }
}
