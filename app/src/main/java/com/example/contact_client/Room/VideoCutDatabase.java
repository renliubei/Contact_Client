package com.example.contact_client.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {VideoCut.class}, version = 1, exportSchema = false)
public abstract class VideoCutDatabase extends RoomDatabase {
    //有多个Dao则要写多个getDao
    public abstract VideoCutDao getVideoCutDao();
}
