package com.example.contact_client.interactive_creator;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoCutDao;

import java.util.List;

public class CreatorViewModel extends AndroidViewModel {

    //互动视频的路径
    private String[] path;
    //访问数据库数据
    private VideoCutDao videoCutDao;
    //显示添加的子节点
    private LiveData<List<VideoCut>> sonLiveDataVideoCuts;
    //当前节点
    private VideoNode videoNode;

    public CreatorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<VideoCut>> getSonLiveDataVideoCuts() {
        return sonLiveDataVideoCuts;
    }

    public void setSonLiveDataVideoCuts(LiveData<List<VideoCut>> sonLiveDataVideoCuts) {
        this.sonLiveDataVideoCuts = sonLiveDataVideoCuts;
    }
}
