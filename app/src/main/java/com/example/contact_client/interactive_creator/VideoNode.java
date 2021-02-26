package com.example.contact_client.interactive_creator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VideoNode implements Parcelable {
    //最后一个到达这个Node的Node在列表中的index
    private int fatherIndex;
    //自身在列表中的index
    private int Index;
    //定位到当前应该视频的在数据库中的Id
    private long Id;
    //子节点在列表中的index
    private List<Integer> sons;
    //
    private String name;

    public VideoNode(int fatherIndex, int Index, long Id, String name) {
        this.fatherIndex = fatherIndex;
        this.Index = Index;
        this.Id = Id;
        this.name = name;
        sons = new ArrayList<>();
    }

    protected VideoNode(Parcel in) {
        fatherIndex = in.readInt();
        Index = in.readInt();
        Id = in.readLong();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fatherIndex);
        dest.writeInt(Index);
        dest.writeLong(Id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoNode> CREATOR = new Creator<VideoNode>() {
        @Override
        public VideoNode createFromParcel(Parcel in) {
            return new VideoNode(in);
        }

        @Override
        public VideoNode[] newArray(int size) {
            return new VideoNode[size];
        }
    };

    public int getFatherIndex() {
        return fatherIndex;
    }


    public void addSons(int sonIndex){
        sons.add(sonIndex);
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public List<Integer> getSons() {
        return sons;
    }

    public void setSons(List<Integer> sons) {
        this.sons = sons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFatherIndex(int fatherIndex) {
        this.fatherIndex = fatherIndex;
    }
}
