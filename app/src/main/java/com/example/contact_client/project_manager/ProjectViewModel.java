package com.example.contact_client.project_manager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contact_client.repository.VideoCutDatabase;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.VideoProjectDao;

import java.util.List;

import io.reactivex.Single;

public class ProjectViewModel extends AndroidViewModel {
    //访问数据库
    private final VideoProjectDao videoProjectDao;
    //保存访问到的project
    private final LiveData<List<VideoProject>>  projectsLiveDataList;
    //保存中心item的position
    private int position = -1;
    //编辑界面的名字提示
    private MutableLiveData<String> editorHintName;
    //编辑界面的描述提示
    private MutableLiveData<String> editorHintDecs;
    //编辑界面的封面提示
    private MutableLiveData<String> hintCover;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setEditorHintName(MutableLiveData<String> editorHintName) {
        this.editorHintName = editorHintName;
    }

    public void setEditorHintDecs(MutableLiveData<String> editorHintDecs) {
        this.editorHintDecs = editorHintDecs;
    }

    public void setHintCover(MutableLiveData<String> hintCover) {
        this.hintCover = hintCover;
    }

    public MutableLiveData<String> getEditorHintName() {
        if(editorHintName==null){
            editorHintName = new MutableLiveData<>();
            editorHintName.setValue("名字");
        }
        return editorHintName;
    }

    public MutableLiveData<String> getEditorHintDecs() {
        if(editorHintDecs==null){
            editorHintDecs = new MutableLiveData<>();
            editorHintDecs.setValue("描述");
        }
        return editorHintDecs;
    }

    public MutableLiveData<String> getHintCover() {
        if(hintCover==null){
            hintCover = new MutableLiveData<>();
        }
        return hintCover;
    }

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return videoProjectDao.insertVideoProject(videoProject);
    }
}
