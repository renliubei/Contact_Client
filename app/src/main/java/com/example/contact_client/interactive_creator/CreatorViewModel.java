package com.example.contact_client.interactive_creator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.contact_client.VideoCut;
import com.example.contact_client.VideoCutDao;

import java.util.List;

public class CreatorViewModel extends ViewModel {
    //存放路径
    private String[] path;
    //访问数据库数据
    private VideoCutDao videoCutDao;
    //更新
    private LiveData<List<VideoCut>> allLiveDataVideoCuts;
}
