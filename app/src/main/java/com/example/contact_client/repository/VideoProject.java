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
        videoNodeList = new ArrayList<>();
        isolatedNodes = new ArrayList<>();
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
        return videoNodeList;
    }

    public void setVideoNodeList(List<VideoNode> videoNodeList) {
        this.videoNodeList = videoNodeList;
    }

    public List<VideoNode> getIsolatedNodes() {
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

    public void deleteNode(int nodeIndex,int fatherIndex){
        if(nodeIndex>=videoNodeList.size()) return;
        VideoNode videoNode = videoNodeList.get(nodeIndex);
        if(fatherIndex>=videoNodeList.size()|| !videoNode.getFathers().contains(fatherIndex)) return;
        _deleteNode(nodeIndex,fatherIndex);
    }

    private void _deleteNode(int nodeIndex,int fatherIndex){
        VideoNode videoNode = videoNodeList.get(nodeIndex);
        //删除父亲
        videoNode.getFathers().remove((Integer) fatherIndex);
        Log.d("mylo",fatherIndex + videoNode.getFathers().toString());
        //根节点不能被孤立
        if(videoNode.getIndex()==0){
            return;
        }
        //如果此结点已经没有任何父亲
        if(videoNode.getFathers().isEmpty()){
            //移动到被删除列表
            videoNode.setId(ISOLATED);
            isolatedNodes.add(videoNode);
            Log.d("mylo","P" + videoNode.getIndex() + " deleted: " + isolatedNodes.toString());
            //递归判断其儿子是否也被孤立
            for(int i=0;i<videoNode.getSons().size();i++){
                _deleteNode(videoNode.getSons().get(i),videoNode.getIndex());
            }
            videoNode.getSons().clear();
        }
    }

    public boolean saveVideoCutsToNode(@NonNull List<VideoCut> list,int nodeIndex){
        if(nodeIndex>=videoNodeList.size()) return false;
        if(list.size()==0) return true;
        return _saveVideoCutsToNode(list,nodeIndex);
    }

    private boolean _saveVideoCutsToNode(@NonNull List<VideoCut> list,int nodeIndex){
        try{
            VideoNode videoNode;
            VideoCut videoCut;
            VideoNode fatherNode = videoNodeList.get(nodeIndex);
            for(int i=0;i<list.size();i++){
                //替换Id
                if(i<fatherNode.getSons().size()){
                    //取出已存的sonNode
                    videoNode = videoNodeList.get(fatherNode.getSons().get(i));
                    //将Id替换为点击的VideoCut的Id
                    videoNode.setId(list.get(i).getId());
                }else{
                    videoCut = list.get(i);
                    //如果有被删除结点，回收
                    if(isolatedNodes.size()>0){
                        videoNode = isolatedNodes.remove(0);
                        videoNode.toDefault();
                        videoNode.setLastNodeIndex(fatherNode.getIndex());
                    }else{
                        //不存在删除结点，新增
                        videoNode = new VideoNode(fatherNode.getIndex(),getListSize(),videoCut.getId());
                        addNode(videoNode);
                    }
                    videoNode.addFather(fatherNode.getIndex());
                    fatherNode.addSon(videoNode.getIndex());
                }
            }
            Log.d("mylo","videoNodes are: "+videoNodeList.toString());
            Log.d("mylo","sons are: "+fatherNode.getSons().toString());
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void addSonNodes(int nodeIndex,List<Integer> newSons){
        if(nodeIndex>=videoNodeList.size()) return;
        for(int i=0;i<newSons.size();i++) if(newSons.get(i)>videoNodeList.size()) return;
        _addSonNodes(nodeIndex,newSons);
    }

    private void _addSonNodes(int nodeIndex,List<Integer> newSons){
        VideoNode node = videoNodeList.get(nodeIndex);
        for(int i=0;i<newSons.size();i++){
            if(!node.getSons().contains(newSons.get(i))){
                node.getSons().add(newSons.get(i));
            }
        }
    }
}