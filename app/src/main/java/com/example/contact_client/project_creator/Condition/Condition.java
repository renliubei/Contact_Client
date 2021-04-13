package com.example.contact_client.project_creator.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 用于结点进入和播放完毕的条件判断
 * 包括初始值，当前值，值的上下限
 */
public class Condition implements Parcelable {
    private String conditionName;
    private int defaultValue;
    private int value;
    private int max,min;

    /**
     * 返回默认参数 默认值50 最大值100 最小值50
     */
    public Condition() {
        this.conditionName  = "新参数";
        this.defaultValue = 50;
        this.value = 50;
        this.max = 100;
        this.min = 0;
    }

    /**
     * 最大值100，最小值0
     * @param conditionName 条件名，作为唯一标识
     * @param defaultValue 默认值
     */
    public Condition(@NonNull String conditionName, int defaultValue) {
        this.conditionName = conditionName;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        max = 100;
        min = 0;
    }

    /**
     * 请保证最大值大于等于最小值
     * @param conditionName 条件名，作为唯一标识
     * @param defaultValue 默认值
     * @param max 最大值
     * @param min 最小值
     */
    public Condition(@NonNull String conditionName, int defaultValue, int max, int min) {
        this.conditionName = conditionName;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.max = max;
        this.min = min;
    }

    protected Condition(Parcel in) {
        conditionName = in.readString();
        defaultValue = in.readInt();
        value = in.readInt();
        max = in.readInt();
        min = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(conditionName);
        dest.writeInt(defaultValue);
        dest.writeInt(value);
        dest.writeInt(max);
        dest.writeInt(min);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Condition> CREATOR = new Creator<Condition>() {
        @Override
        public Condition createFromParcel(Parcel in) {
            return new Condition(in);
        }

        @Override
        public Condition[] newArray(int size) {
            return new Condition[size];
        }
    };

    /**
     * 使条件值恢复默认
     */
    public void toDefault(){
        value = defaultValue;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getValue() {
        return value;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    /**
     * 改变条件值，不会超出大小界限
     * @param changeBy 改变幅度
     */
    protected void changeValue(int changeBy){
        value += changeBy;
        if(value<min) value = min;
        else if(value>max) value = max;
    }

}
