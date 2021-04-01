package com.example.contact_client.interactive_creator.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ConditionJudge implements Parcelable {
    private Condition condition;
    private int judge;

    /**
     *
     * @param condition 相应条件
     * @param judge 判断方式
     */
    public ConditionJudge(@NonNull Condition condition, int judge) {
        this.condition = condition;
        this.judge = judge;
    }

    protected ConditionJudge(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
        judge = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(condition, flags);
        dest.writeInt(judge);
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

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }
}
