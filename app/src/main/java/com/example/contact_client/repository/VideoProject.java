package com.example.contact_client.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.contact_client.interactive_creator.VideoNode;

import java.util.ArrayList;
import java.util.List;

@Entity
public class VideoProject {
    @PrimaryKey(autoGenerate = true)
    private long Id;

    @ColumnInfo
    private List<VideoNode> videoNodeList;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public List<VideoNode> getVideoNodeList() {
        if(videoNodeList==null){
            videoNodeList = new ArrayList<>();
        }
        return videoNodeList;
    }

    public void setVideoNodeList(List<VideoNode> videoNodeList) {
        this.videoNodeList = videoNodeList;
    }

}