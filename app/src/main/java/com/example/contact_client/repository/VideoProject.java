package com.example.contact_client.repository;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.contact_client.interactive_creator.VideoNode;
import com.example.contact_client.repository.type_converter.VideoNodeListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(VideoNodeListConverter.class)
public class VideoProject implements Parcelable{

    @Ignore
    public static final int ISOLATED = -2;

    @PrimaryKey(autoGenerate = true)
    private long Id;

    @ColumnInfo
    private List<VideoNode> videoNodeList;

    @ColumnInfo
    private List<VideoNode> isolatedNodes;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String description;

    //todo:change to internet url
    @ColumnInfo
    private String coverUrl;

    public VideoProject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected VideoProject(Parcel in) {
        Id = in.readLong();
        videoNodeList = in.createTypedArrayList(VideoNode.CREATOR);
        isolatedNodes = in.createTypedArrayList(VideoNode.CREATOR);
        name = in.readString();
        description = in.readString();
        coverUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeTypedList(videoNodeList);
        dest.writeTypedList(isolatedNodes);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(coverUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoProject> CREATOR = new Creator<VideoProject>() {
        @Override
        public VideoProject createFromParcel(Parcel in) {
            return new VideoProject(in);
        }

        @Override
        public VideoProject[] newArray(int size) {
            return new VideoProject[size];
        }
    };

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public List<VideoNode> getVideoNodeList() {
        if(videoNodeList==null){
            videoNodeList = new ArrayList<>();
        }
        return videoNodeList;
    }

    public void setVideoNodeList(List<VideoNode> videoNodeList) {
        this.videoNodeList = videoNodeList;
    }

    public List<VideoNode> getIsolatedNodes() {
        if(isolatedNodes ==null){
            isolatedNodes = new ArrayList<>();
        }
        return isolatedNodes;
    }

    public void setIsolatedNodes(List<VideoNode> isolatedNodes) {
        this.isolatedNodes = isolatedNodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addNode(VideoNode videoNode){
        getVideoNodeList().add(videoNode);
    }

    public int getListSize(){
        return getVideoNodeList().size();
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "id: " + Id + "\t" + "name:" +name + "\t" + videoNodeList.toString()+"\t"+coverUrl;
    }

    public void deleteNode(@NonNull VideoNode videoNode,int fatherIndex){
        if(fatherIndex>videoNode.getSons().size()) return;
        //删除父亲
        videoNode.getFathers().remove(fatherIndex);
        //根节点不能被孤立
        if(videoNode.getIndex()==0){
            return;
        }
        //如果此结点已经没有任何父亲
        if(videoNode.getFathers().size()==0){
            //移动到被删除列表
            videoNode.setId(ISOLATED);
            isolatedNodes.add(videoNode);
            Log.d("mylo","P" + videoNode.getIndex() + " deleted: " + isolatedNodes.toString());
            //递归判断其儿子是否也被孤立
            VideoNode son;
            for(int i=0;i<videoNode.getSons().size();i++){
                son = videoNodeList.get(videoNode.getSons().get(i));
                deleteNode(son,son.getSons().indexOf(videoNode));
            }
            videoNode.getSons().clear();
        }
    }
}