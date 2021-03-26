package com.example.contact_client.repository.type_converter;

import androidx.room.TypeConverter;

import com.example.contact_client.interactive_creator.VideoNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoNodeListConverter {
    @TypeConverter
    public List<VideoNode> revert(String jsonString){
        try {
            Type type = new TypeToken<ArrayList<VideoNode>>(){}.getType();
            return new Gson().fromJson(jsonString,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public String converter(List<VideoNode> list){
        return new Gson().toJson(list);
    }
}
