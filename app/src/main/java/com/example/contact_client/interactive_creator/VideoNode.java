package com.example.contact_client.interactive_creator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VideoNode implements Parcelable {
    private static final String DEFAULT_NAME = "新节点";
    private static final String DEFAULT_PLOT = "从前有座山，山上有座庙";
    private static final String DEFAULT_BTN_TEXT = "睡上一觉";
    //TODO:返回爹
    private final List<Integer> fathers = new ArrayList<>();
    //子节点在列表中的index
    private final List<Integer> sons = new ArrayList<>();
    //最后一个到达这个Node的Node在列表中的index
    private int lastNodeIndex;
    //自身在列表中的index
    private int index;
    //定位到当前应该视频的在数据库中的Id
    private long Id;
    //结点名称
    private String nodeName;
    //结点选项提示
    private String btnText;
    //结点剧情
    private String plot;

    public VideoNode(int lastNodeIndex,int index, long id) {
        this.index = index;
        this.lastNodeIndex = lastNodeIndex;
        Id = id;
        toDefault();
    }

    protected VideoNode(Parcel in) {
        lastNodeIndex = in.readInt();
        index = in.readInt();
        Id = in.readLong();
        nodeName = in.readString();
        btnText = in.readString();
        plot = in.readString();
        in.readList(fathers,Integer.class.getClassLoader());
        in.readList(sons,Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lastNodeIndex);
        dest.writeInt(index);
        dest.writeLong(Id);
        dest.writeString(nodeName);
        dest.writeString(btnText);
        dest.writeString(plot);
        dest.writeList(fathers);
        dest.writeList(sons);
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
        return sons;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getBtnText() {
        return btnText;
    }

    public String getPlot() {
        return plot;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setLastNodeIndex(int lastNodeIndex) {
        this.lastNodeIndex = lastNodeIndex;
    }

    public void setId(long id) {
        Id = id;
    }

    public void addSon(int sonIndex){
        getSons().add(sonIndex);
    }

    public void addFather(int fatherIndex){
        getFathers().add(fatherIndex);
    }

    public void toDefault(){
        nodeName  = DEFAULT_NAME;
        btnText = DEFAULT_BTN_TEXT;
        plot = DEFAULT_PLOT;
    }
}
