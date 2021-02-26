package com.example.contact_client.interactive_creator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VideoNode implements Parcelable {
    //父亲在列表中的index
    private int fatherVideoCutIndex;
    //自身在列表中的index
    private int Index;
    //定位到当前应该视频的在数据库中的Id
    private long Id;
    //子节点在列表中的index
    private List<Integer> sons;
    //
    private boolean deleted;
    //
    private String name;

    public VideoNode(int fatherVideoCutIndex, int Index, long Id,String name) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
        this.Index = Index;
        this.Id = Id;
        this.deleted = false;
        this.name = name;
        sons = new ArrayList<>();
    }

    protected VideoNode(Parcel in) {
        fatherVideoCutIndex = in.readInt();
        Index = in.readInt();
        Id = in.readLong();
        deleted = in.readByte() != 0;
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fatherVideoCutIndex);
        dest.writeInt(Index);
        dest.writeLong(Id);
        dest.writeByte((byte) (deleted ? 1 : 0));
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

    public int getFatherVideoCutIndex() {
        return fatherVideoCutIndex;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFatherVideoCutIndex(int fatherVideoCutIndex) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
    }
}
