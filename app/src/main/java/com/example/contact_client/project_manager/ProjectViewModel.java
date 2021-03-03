package com.example.contact_client.project_manager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contact_client.repository.VideoCutDatabase;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.VideoProjectDao;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {
    //访问数据库
    private final VideoProjectDao videoProjectDao;
    //保存访问到的project
    private final LiveData<List<VideoProject>>  projectsLiveDataList;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        //绑定数据库
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(application);
        videoProjectDao = videoCutDatabase.getVideoProjectDao();
        projectsLiveDataList = videoProjectDao.getAllLiveDataVideoProjects();
    }

    public LiveData<List<VideoProject>> getProjectsLiveDataList() {
        return projectsLiveDataList;
    }
}
