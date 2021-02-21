package com.example.contact_client.interactive_creator;

import java.util.ArrayList;
import java.util.List;

public class VideoNode {
    //父亲在列表中的index
    private long fatherVideoCutIndex;
    //自身在列表中的index
    private long currentIndex;
    //定位到当前应该视频的在数据库中的Id
    private long currentId;
    //子节点在列表中的index
    private List<Integer> sons;

    public VideoNode(long fatherVideoCutIndex, long currentIndex, long currentId) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
        this.currentIndex = currentIndex;
        this.currentId = currentId;
        sons = new ArrayList<>();
    }

    public long getFatherVideoCutIndex() {
        return fatherVideoCutIndex;
    }

    public void setFatherVideoCutIndex(long fatherVideoCutIndex) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
    }

    public long getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(long currentIndex) {
        this.currentIndex = currentIndex;
    }

    public long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
    }

    public List<Integer> getSons() {
        return sons;
    }

    public void setSons(List<Integer> sons) {
        this.sons = sons;
    }

    public void addSons(int sonIndex){
        sons.add(sonIndex);
    }
}
