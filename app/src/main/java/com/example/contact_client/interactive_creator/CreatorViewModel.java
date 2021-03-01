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
    //访问数据库
    private final VideoCutDao videoCutDao;
    private final VideoProjectDao videoProjectDao;
    //当前的互动视频
    private VideoProject videoProject;
    //根节点的VideoCut
    private final VideoCut rootVideoCut;

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
        //
        rootVideoCut = new VideoCut(true,"根结点","这是根节点",null,null);
        rootVideoCut.setId(-1);
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

    public VideoCut getRootVideoCut() {
        return rootVideoCut;
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

    public Single<VideoCut> getVideoCutById(long id){return videoCutDao.findById(id);}

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return videoProjectDao.insertVideoProject(videoProject);
    }

}
