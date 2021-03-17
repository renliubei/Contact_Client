package com.example.contact_client.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class mRepository {
    private VideoCutDao videoCutDao;
    private VideoProjectDao videoProjectDao;

    public mRepository(Context context) {
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(context);
        videoCutDao = videoCutDatabase.getVideoCutDao();
        videoProjectDao = videoCutDatabase.getVideoProjectDao();
    }

    public Single<VideoProject> findVideoProjectById(long id){
        return videoProjectDao.findById(id);
    }

    public Single<Long> insertVideoProject(VideoProject videoProject){
        return videoProjectDao.insertVideoProject(videoProject);
    }

    public LiveData<List<VideoProject>> getAllLiveDataVideoProjects(){
        return videoProjectDao.getAllLiveDataVideoProjects();
    }

    public Single<List<Long>> insertVideoCuts(List<VideoCut> list){
        return videoCutDao.insertVideoCuts(list);
    }

    public Single<Integer> updateVideoCuts(VideoCut... videoCuts){
        return videoCutDao.updateVideoCuts(videoCuts);
    }

    public Single<Integer> deleteVideoCuts(VideoCut... videoCuts){
        return videoCutDao.deleteVideoCuts(videoCuts);
    }

    public Completable deleteAllVideoCuts(){
        return videoCutDao.deleteAllVideoCuts();
    }


    public LiveData<List<VideoCut>> getAllLiveDataVideoCuts(){
        return videoCutDao.getAllLiveDataVideoCuts();
    }

    public Single<VideoCut> findVideoCutById(long id){
        return videoCutDao.findById(id);
    }

    public Single<List<VideoCut>> getAllVideoCutById(List<Long> Ids){
        return videoCutDao.getAllById(Ids);
    }

}
