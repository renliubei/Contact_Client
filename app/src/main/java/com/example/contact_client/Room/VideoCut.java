package com.example.contact_client.Room;

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
    @ColumnInfo(name = "lenth")
    private int lenth;

    public VideoCut(String name, String description, int lenth) {
        this.name = name;
        this.description = description;
        this.lenth = lenth;
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

    public int getLenth() {
        return lenth;
    }

    public void setLenth(int lenth) {
        this.lenth = lenth;
    }
}
