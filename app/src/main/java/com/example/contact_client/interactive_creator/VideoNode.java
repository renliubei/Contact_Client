package com.example.contact_client.interactive_creator;

import java.util.ArrayList;
import java.util.List;

public class VideoNode {
    //父亲在列表中的index
    private int fatherVideoCutIndex;
    //自身在列表中的index
    private int Index;
    //定位到当前应该视频的在数据库中的Id
    private long Id;
    //子节点在列表中的index
    private List<Integer> sons;

    public VideoNode(int fatherVideoCutIndex, int Index, long Id) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
        this.Index = Index;
        this.Id = Id;
        sons = new ArrayList<>();
    }

    public int getFatherVideoCutIndex() {
        return fatherVideoCutIndex;
    }

    public void setFatherVideoCutIndex(int fatherVideoCutIndex) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
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
}
