package com.example.contact_client.interactive_creator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VideoNode implements Parcelable {
    //TODO:返回爹
    private List<Integer> fathers;
    //最后一个到达这个Node的Node在列表中的index
    private int lastNodeIndex;
    //自身在列表中的index
    private int index;
    //定位到当前应该视频的在数据库中的Id
    private long Id;
    //子节点在列表中的index
    private List<Integer> sons;
    //
    private String name;

    public VideoNode(int lastNodeIndex, int index, long id, String name) {
        this.lastNodeIndex = lastNodeIndex;
        this.index = index;
        Id = id;
        this.name = name;
    }

    protected VideoNode(Parcel in) {
        lastNodeIndex = in.readInt();
        index = in.readInt();
        Id = in.readLong();
        name = in.readString();
        in.readList(getSons(),Integer.class.getClassLoader());
        in.readList(getFathers(),Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lastNodeIndex);
        dest.writeInt(index);
        dest.writeLong(Id);
        dest.writeString(name);
        dest.writeList(getSons());
        dest.writeList(getFathers());
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

    public List<Integer> getFathers() {
        if(fathers ==null){
            fathers = new ArrayList<>();
        }
        return fathers;
    }

    public int getLastNodeIndex() {
        return lastNodeIndex;
    }

    public int getIndex() {
        return index;
    }

    public long getId() {
        return Id;
    }

    public List<Integer> getSons() {
        if(sons==null){
            sons = new ArrayList<>();
        }
        return sons;
    }

    public String getName() {
        return name;
    }

    public void setFathers(List<Integer> fathers) {
        this.fathers = fathers;
    }

    public void setLastNodeIndex(int lastNodeIndex) {
        this.lastNodeIndex = lastNodeIndex;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setSons(List<Integer> sons) {
        this.sons = sons;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName()+", sons: "+getSons().toString();
    }

    public void addSon(int sonIndex){
        getSons().add(sonIndex);
    }

    public void addFather(int fatherIndex){
        getFathers().add(fatherIndex);
    }
}
