package com.example.contact_client.interactive_creator;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoCutDao;
import com.example.contact_client.repository.VideoCutDatabase;
import com.example.contact_client.repository.VideoProject;

import java.util.ArrayList;
import java.util.List;

public class CreatorViewModel extends AndroidViewModel {

    //互动视频的路径
    private String[] path;
    //显示添加的子节点
    private final List<VideoCut> sonVideoCuts;
    //当前节点
    private VideoNode videoNode;
    //
    private VideoCutDao videoCutDao;
    //
    private VideoProject videoProject;

    public CreatorViewModel(@NonNull Application application) {
        super(application);
        sonVideoCuts = new ArrayList<>();
        videoProject = new VideoProject();
        //最初的根节点
        videoNode = new VideoNode(-1,0,-1);
        videoProject.addNode(videoNode);
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(application);
        videoCutDao = videoCutDatabase.getVideoCutDao();
    }

    public List<VideoCut> getSonVideoCuts() {
        return sonVideoCuts;
    }

    public String[] getPath() {
        return path;
    }

    public VideoNode getVideoNode() {
        //起始的根节点
        return videoNode;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public void setVideoNode(VideoNode videoNode) {
        this.videoNode = videoNode;
    }

    public VideoProject getVideoProject() {
        return videoProject;
    }

    public void setVideoProject(VideoProject videoProject) {
        this.videoProject = videoProject;
    }
}
