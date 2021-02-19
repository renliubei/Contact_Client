package com.example.contact_client.interactive_creator;

public class VideoNode {
    //父亲在列表中的index
    private long fatherVideoCutIndex;
    //定位到当前应该视频的在数据库中的Id
    private long currentId;
    //应该跳转到的视频在表中的Index
    private String[] sonsListIndex;

    public VideoNode(long fatherVideoCutIndex, long currentId, String[] sonsListIndex) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
        this.currentId = currentId;
        this.sonsListIndex = sonsListIndex;
    }

    public long getFatherVideoCutIndex() {
        return fatherVideoCutIndex;
    }

    public void setFatherVideoCutIndex(long fatherVideoCutIndex) {
        this.fatherVideoCutIndex = fatherVideoCutIndex;
    }

    public String[] getSonsListIndex() {
        return sonsListIndex;
    }

    public void setSonsListIndex(String[] sonsListIndex) {
        this.sonsListIndex = sonsListIndex;
    }
}
