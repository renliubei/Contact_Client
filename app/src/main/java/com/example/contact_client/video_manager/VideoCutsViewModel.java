package com.example.contact_client.video_manager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoCutDao;
import com.example.contact_client.repository.VideoCutDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class VideoCutsViewModel extends AndroidViewModel {
    //
    private VideoCutDao videoCutDao;

    private LiveData<List<VideoCut>> allLiveDataVideoCuts;
    //

    public VideoCutsViewModel(@NonNull Application application) {
        super(application);
        VideoCutDatabase videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(application);
        videoCutDao = videoCutDatabase.getVideoCutDao();
        allLiveDataVideoCuts = videoCutDao.getAllLiveDataVideoCuts();
    }

    public LiveData<List<VideoCut>> getAllLiveDataVideoCuts() {
        return allLiveDataVideoCuts;
    }

    public Single<List<Long>> InsertVideoCuts(List<VideoCut> videoCuts) {
        return videoCutDao.insertVideoCuts(videoCuts);
    }

    public Completable ClearVideoCuts() {
        return videoCutDao.deleteAllVideoCuts();
    }

    public Single<Integer> DeleteVideoCuts(VideoCut... videoCuts) {
        return videoCutDao.deleteVideoCuts(videoCuts);
    }

    public Single<Integer> UpdateVideoCuts(VideoCut... videoCuts) {
        return videoCutDao.updateVideoCuts(videoCuts);
    }
}
