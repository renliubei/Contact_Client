package com.example.contact_client.interactive_creator;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.mRepository;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Single;

public class CreatorViewModel extends AndroidViewModel {

    //todo：path的相关逻辑
    //显示添加的子节点
    private final List<VideoCut> sonVideoCuts;
    //当前节点
    private MutableLiveData<VideoNode> videoNodeMutableLiveData;
    //访问数据库
    private final mRepository mRepository;
    //当前的互动视频
    private VideoProject videoProject;
    //根节点的VideoCut
    private final VideoCut rootVideoCut;

    public CreatorViewModel(@NonNull Application application) {
        super(application);
        //初始化列表
        sonVideoCuts = new ArrayList<>();
        //绑定数据库
        mRepository = new mRepository(application);
        //用于显示根节点
        rootVideoCut = new VideoCut(true,"根结点","这是根节点",null,null);
        rootVideoCut.setId(-1);
        //
        videoNodeMutableLiveData = new MutableLiveData<>();
    }

    public List<VideoCut> getSonVideoCuts() {
        return sonVideoCuts;
    }

    public VideoNode getVideoNode() {
        return videoNodeMutableLiveData.getValue();
    }

    public VideoCut getRootVideoCut() {
        return rootVideoCut;
    }

    public MutableLiveData<VideoNode> getVideoNodeMutableLiveData() {
        return videoNodeMutableLiveData;
    }

    //setter
    //

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
        return mRepository.getAllVideoCutById(ids);
    }

    public Single<VideoCut> getVideoCutById(long id){return mRepository.findVideoCutById(id);}

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return mRepository.insertVideoProject(videoProject);
    }
    //...

    //
    public void deleteNode(int nodeIndex,int fatherIndex){
        videoProject.deleteNode(nodeIndex,fatherIndex);
    }
    public boolean saveVideoCutsToCurrentNode(){
        return videoProject.saveVideoCutsToNode(sonVideoCuts,getVideoNode());
    }
    public void setCurrentNode(int nodeIndex){
        videoNodeMutableLiveData.setValue(videoProject.getVideoNodeList().get(nodeIndex));
    }
    public void setCurrentNode(VideoNode node){
        videoNodeMutableLiveData.setValue(node);
    }
    public void addSonNodes(List<Integer> sonIndexes){
        videoProject.addSonNodes(getVideoNode().getIndex(),sonIndexes);
    }

    /**
     * 跳转到新节点
     * @param index 新节点下标
     */
    public void jumpToNode(int index) {
        if(index>videoProject.getVideoNodeList().size()) return;
        VideoNode newNode = videoProject.getVideoNodeList().get(index);
        newNode.setLastNodeIndex(getVideoNode().getIndex());
        setCurrentNode(newNode);
        Toasty.info(getApplication(), "跳转到结点P"+index, Toast.LENGTH_SHORT,true).show();
    }
}
