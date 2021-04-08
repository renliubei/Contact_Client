package com.example.contact_client.repository.type_converter;

import androidx.room.TypeConverter;

import com.example.contact_client.project_creator.Condition.Condition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConditionListConverter{
    @TypeConverter
    public List<Condition> revert(String jsonString){
        try {
            Type type = new TypeToken<ArrayList<Condition>>(){}.getType();
            return new Gson().fromJson(jsonString,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public String converter(List<Condition> list){
        return new Gson().toJson(list);
    }
}
