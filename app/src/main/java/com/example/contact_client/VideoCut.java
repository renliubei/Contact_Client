package com.example.contact_client;

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
    @ColumnInfo(name = "duration" )
    private int duration;
    @ColumnInfo(name = "uriString" )
    private String uriString;

    public VideoCut(boolean isCut, String name, String description, int duration, String uriString) {
        this.isCut = isCut;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.uriString = uriString;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
