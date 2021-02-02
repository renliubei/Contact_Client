package com.example.contact_client.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VideoCutDao {
    //用于对数据库中VideoCut表进行操作
    @Insert
    void insertVideoCuts(VideoCut... videoCuts);

    @Update
    void updateVideoCuts(VideoCut... videoCuts);

    @Delete
    void deleteVideoCuts(VideoCut... videoCuts);

    @Query("DELETE FROM VideoCut")
    void deleteAllVideoCuts();

    @Query("SELECT * FROM VideoCut ORDER BY ID DESC")
    List<VideoCut> getAllVideoCuts();
}

