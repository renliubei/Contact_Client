package com.example.contact_client.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface VideoProjectDao {

    @Query("select * from VideoProject where id=:id")
    public Single<VideoProject> findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Single<Long> insertVideoProject(VideoProject videoProject);

    @Query("SELECT * FROM VideoProject ORDER BY ID DESC")
    public LiveData<List<VideoProject>> getAllLiveDataVideoProjects();

    @Delete
    public Single<Integer> deleteProject(VideoProject videoProject);
}
