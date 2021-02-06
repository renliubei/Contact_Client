package com.example.contact_client;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
        allLiveDataVideoCuts = videoCutDao.getAllVideoCuts();
    }

    public LiveData<List<VideoCut>> getAllLiveDataVideoCuts() {
        return allLiveDataVideoCuts;
    }

    public Single<List<Long>> InsertVideoCuts(VideoCut... videoCuts) {
        return videoCutDao.insertVideoCuts(videoCuts);
    }

    public Completable ClearVideoCuts() {
        return videoCutDao.deleteAllVideoCuts();
    }

    public Single<Integer> DeleteVideoCuts(VideoCut videoCut) {
        return videoCutDao.deleteVideoCuts(videoCut);
    }

    public Single<Integer> UpdateVideoCuts(VideoCut videoCut) {
        return videoCutDao.updateVideoCuts(videoCut);
    }
}
