package com.example.contact_client.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface VideoCutDao {
    //用于对数据库中VideoCut表进行操作
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Single<List<Long>> insertVideoCuts(List<VideoCut> list);

    @Update
    public Single<Integer> updateVideoCuts(VideoCut... videoCuts);

    @Delete
    public Single<Integer> deleteVideoCuts(VideoCut... videoCuts);

    @Query("DELETE FROM VideoCut")
    public Completable deleteAllVideoCuts();

    @Delete
    public Single<Integer> deleteUsers(VideoCut... videoCuts);

    @Query("SELECT * FROM VideoCut ORDER BY ID DESC")
    public LiveData<List<VideoCut>> getAllLiveDataVideoCuts();

    @Query("select * from VideoCut where id=:id")
    public Single<VideoCut> findById(long id);

    @Query("SELECT * FROM VideoCut WHERE id IN (:Ids)")
    public Single<List<VideoCut>> getAllById(List<Long> Ids);
}

