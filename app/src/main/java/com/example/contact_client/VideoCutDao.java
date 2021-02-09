package com.example.contact_client;

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
    Single<List<Long>> insertVideoCuts(List<VideoCut> list);

    @Update
    Single<Integer> updateVideoCuts(VideoCut... videoCuts);

    @Delete
    Single<Integer> deleteVideoCuts(VideoCut... videoCuts);

    @Query("DELETE FROM VideoCut")
    Completable deleteAllVideoCuts();

    @Delete
    Single<Integer> deleteUsers(VideoCut... videoCuts);

    @Query("SELECT * FROM VideoCut ORDER BY ID DESC")
    LiveData<List<VideoCut>> getAllVideoCuts();
}

