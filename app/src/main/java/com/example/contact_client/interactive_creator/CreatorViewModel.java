package com.example.contact_client.interactive_creator;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contact_client.repository.VideoCut;

import java.util.ArrayList;
import java.util.List;

public class CreatorViewModel extends AndroidViewModel {

    //互动视频的路径
    private String[] path;
    //显示添加的子节点
    private MutableLiveData<List<VideoCut>> sonVideoCuts;
    //当前节点
    private VideoNode videoNode;

    public CreatorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<VideoCut>> getSonVideoCuts() {
        if(sonVideoCuts==null){
            sonVideoCuts = new MutableLiveData<>();
            sonVideoCuts.setValue(new ArrayList<>());
        }
        return sonVideoCuts;
    }

    public String[] getPath() {
        return path;
    }

    public VideoNode getVideoNode() {
        if(videoNode==null){
            videoNode = new VideoNode(-1,0,-1,null);
        }
        return videoNode;
    }
}
