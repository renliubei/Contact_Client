package com.example.contact_client.repository.type_converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseListConverter<T> {
    @TypeConverter
    public List<T> revert(String jsonString){
        try {
            Type type = new TypeToken<ArrayList<T>>(){}.getType();
            return new Gson().fromJson(jsonString,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public String converter(List<T> list){
        return new Gson().toJson(list);
    }
}
