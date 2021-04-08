package com.example.contact_client.project_creator.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ConditionJudge implements Parcelable {
    private Condition condition;
    private int judge;
    private int requiredValue;

    /**
     *
     * @param condition 相应条件
     * @param judge 判断方式
     * @param requiredValue 要求达到的值
     */
    public ConditionJudge(@NonNull Condition condition, int judge, int requiredValue) {
        this.condition = condition;
        this.judge = judge;
        this.requiredValue = requiredValue;
    }


    protected ConditionJudge(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
        judge = in.readInt();
        requiredValue = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(condition, flags);
        dest.writeInt(judge);
        dest.writeInt(requiredValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConditionJudge> CREATOR = new Creator<ConditionJudge>() {
        @Override
        public ConditionJudge createFromParcel(Parcel in) {
            return new ConditionJudge(in);
        }

        @Override
        public ConditionJudge[] newArray(int size) {
            return new ConditionJudge[size];
        }
    };

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public int getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(int requiredValue) {
        this.requiredValue = requiredValue;
    }

    public int getCurrentValue(){
        return condition.getValue();
    }
}
