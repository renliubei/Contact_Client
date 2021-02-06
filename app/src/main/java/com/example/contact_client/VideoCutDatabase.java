package com.example.contact_client;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {VideoCut.class}, version = 1, exportSchema = false)
public abstract class VideoCutDatabase extends RoomDatabase {
    //单例化
    private static VideoCutDatabase videoCutDatabase;

    static synchronized VideoCutDatabase getVideoCutDatabase(Context context) {
        if (videoCutDatabase == null) {
            videoCutDatabase = Room.databaseBuilder(context.getApplicationContext(), VideoCutDatabase.class, "videoCutDatabase")
                    .build();
        }
        return videoCutDatabase;
    }

    //有多个Dao则要写多个getDao
    public abstract VideoCutDao getVideoCutDao();
}
