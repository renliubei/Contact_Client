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
    //todo：path的相关逻辑

    //互动视频的路径
    private List<String> path;
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
        path = new ArrayList<>();
        //绑定数据库
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(application);
        videoCutDao = videoCutDatabase.getVideoCutDao();
        videoProjectDao = videoCutDatabase.getVideoProjectDao();
        //用于显示根节点
        rootVideoCut = new VideoCut(true,"根结点","这是根节点",null,null);
        rootVideoCut.setId(-1);
    }

    public List<VideoCut> getSonVideoCuts() {
        return sonVideoCuts;
    }

    public VideoNode getVideoNode() {
        return videoNode;
    }

    public VideoCut getRootVideoCut() {
        return rootVideoCut;
    }

    public List<String> getPath() {
        return path;
    }

    //setter
    //
    public void setPath(List<String> path) {
        this.path = path;
    }

    public VideoProject getVideoProject() {
        return videoProject;
    }

    public void setVideoProject(VideoProject videoProject) {
        this.videoProject = videoProject;
    }
    //

    //对数据库的操作
    //...
    public Single<List<VideoCut>> getAllById(List<Long> ids) {
        return videoCutDao.getAllById(ids);
    }

    public Single<VideoCut> getVideoCutById(long id){return videoCutDao.findById(id);}

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return videoProjectDao.insertVideoProject(videoProject);
    }
    public Single<VideoProject> findProjectById(long id){
        return videoProjectDao.findById(id);
    }
    //...

    //
    public void deleteNode(int nodeIndex,int fatherIndex){
        videoProject.deleteNode(nodeIndex,fatherIndex);
    }
    public boolean saveVideoCutsToCurrentNode(){
        return videoProject.saveVideoCutsToNode(sonVideoCuts,videoNode.getIndex());
    }
    public void setCurrentNode(int nodeIndex){
        videoNode = videoProject.getVideoNodeList().get(nodeIndex);
    }
    public void addSonNodes(List<Integer> sonIndexes){
        videoProject.addSonNodes(videoNode.getIndex(),sonIndexes);
    }
}
