package com.example.contact_client.project_creator.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ConditionJudge implements Parcelable {
    public static final int  OVER = 1;
    public static final int BELOW = 2;
    public static final int EQUAL = 3;
    private Condition condition;
    private int judgeWay;
    private int requiredValue;


    /**
     * 判断一个是否满足条件
     * @return true表示结点满足到达条件
     */
    public boolean judgeNode(){
        switch (judgeWay){
            case OVER: return requiredValue<condition.getValue();
            case BELOW:return requiredValue>condition.getValue();
            case EQUAL:return requiredValue==condition.getValue();
        }
        return true;
    }

    /**
     *
     * @param condition 相应条件
     * @param judgeWay 判断方式
     * @param requiredValue 要求达到的值
     */
    public ConditionJudge(@NonNull Condition condition, int judgeWay, int requiredValue) {
        this.condition = condition;
        this.judgeWay = judgeWay;
        this.requiredValue = requiredValue;
    }

    protected ConditionJudge(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
        judgeWay = in.readInt();
        requiredValue = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(condition, flags);
        dest.writeInt(judgeWay);
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

    public int getJudgeWay() {
        return judgeWay;
    }

    public void setJudgeWay(int judgeWay) {
        this.judgeWay = judgeWay;
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

    @Override
    public String toString() {
        return "ConditionJudge{" +
                "condition=" + condition +
                ", judgeWay=" + judgeWay +
                ", requiredValue=" + requiredValue +
                '}';
    }
}
