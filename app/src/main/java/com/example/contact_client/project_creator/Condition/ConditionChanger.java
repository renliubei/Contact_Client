package com.example.contact_client.project_creator.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ConditionChanger implements Parcelable {
    private Condition condition;
    private int change;

    /**
     *
     * @param condition 相应条件
     * @param change 判断方式
     */
    public ConditionChanger(@NonNull Condition condition, int change) {
        this.condition = condition;
        this.change = change;
    }

    public int getChange() {
        return change;
    }

    protected ConditionChanger(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
        change = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(condition, flags);
        dest.writeInt(change);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConditionChanger> CREATOR = new Creator<ConditionChanger>() {
        @Override
        public ConditionChanger createFromParcel(Parcel in) {
            return new ConditionChanger(in);
        }

        @Override
        public ConditionChanger[] newArray(int size) {
            return new ConditionChanger[size];
        }
    };

    /**
     * 改变条件值，不会超出大小界限
     */
    public void doChange(){
        condition.changeValue(change);
    }

    public Condition getCondition() {
        return condition;
    }
}
