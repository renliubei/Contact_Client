package com.example.contact_client.interactive_creator;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoCutDao;
import com.example.contact_client.repository.VideoCutDatabase;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.VideoProjectDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class CreatorViewModel extends AndroidViewModel {

    //互动视频的路径
    private String[] path;
    //显示添加的子节点
    private final List<VideoCut> sonVideoCuts;
    //当前节点
    private VideoNode videoNode;
    //
    private final VideoCutDao videoCutDao;
    private final VideoProjectDao videoProjectDao;
    //
    private VideoProject videoProject;

    public CreatorViewModel(@NonNull Application application) {
        super(application);
        //初始化列表
        sonVideoCuts = new ArrayList<>();
        videoProject = new VideoProject();
        //最初的根节点
        videoNode = new VideoNode(-1,0,-1,"root");
        videoProject.addNode(videoNode);
        //绑定数据库
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(application);
        videoCutDao = videoCutDatabase.getVideoCutDao();
        videoProjectDao = videoCutDatabase.getVideoProjectDao();
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

    public Single<List<VideoCut>> getAllById(List<Long> ids) {
        return videoCutDao.getAllById(ids);
    }

    public Single<VideoCut> getById(long id){return videoCutDao.findById(id);}

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return videoProjectDao.insertVideoProject(videoProject);
    }
}
