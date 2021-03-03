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
    private List<VideoNode> isolatedNodes;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String description;

    @ColumnInfo
    private String coverUrl;

    public VideoProject(String name, String description) {
        this.name = name;
        this.description = description;
    }

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

    public List<VideoNode> getIsolatedNodes() {
        if(isolatedNodes ==null){
            isolatedNodes = new ArrayList<>();
        }
        return isolatedNodes;
    }

    public void setIsolatedNodes(List<VideoNode> isolatedNodes) {
        this.isolatedNodes = isolatedNodes;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}