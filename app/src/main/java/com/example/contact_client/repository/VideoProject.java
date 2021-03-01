package com.example.contact_client.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.contact_client.interactive_creator.VideoNode;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(VideoNodeListConverter.class)
public class VideoProject {

    @PrimaryKey(autoGenerate = true)
    private long Id;

    @ColumnInfo
    private List<VideoNode> videoNodeList;

    @ColumnInfo
    private List<VideoNode> deletedNodes;

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

    public List<VideoNode> getDeletedNodes() {
        if(deletedNodes==null){
            deletedNodes = new ArrayList<>();
        }
        return deletedNodes;
    }

    public void setDeletedNodes(List<VideoNode> deletedNodes) {
        this.deletedNodes = deletedNodes;
    }

    public void addNode(VideoNode videoNode){
        getVideoNodeList().add(videoNode);
    }

    public void deleteNode(VideoNode videoNode){
        getVideoNodeList().remove(videoNode);
    }

    public void deleteNodeById(int id){
        getVideoNodeList().remove(id);
    }

    public int getListSize(){
        return getVideoNodeList().size();
    }
}