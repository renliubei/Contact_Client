package com.example.contact_client.project_creator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.contact_client.project_creator.Condition.Condition;
import com.example.contact_client.project_creator.Condition.ConditionChanger;
import com.example.contact_client.project_creator.Condition.ConditionJudge;

import java.util.ArrayList;
import java.util.List;

/**
 * 互动视频每个结点的信息
 */
public class VideoNode implements Parcelable {
    private static final String DEFAULT_NAME = "新节点";
    private static final String DEFAULT_PLOT = "默认剧情";
    private static final String DEFAULT_BTN_TEXT = "默认选项内容";
    //TODO:返回爹
    private  List<Integer> fathers = new ArrayList<>();
    //子节点在列表中的index
    private  List<Integer> sons = new ArrayList<>();
    //条件判断者
    private List<ConditionJudge> judges = new ArrayList<>();
    //条件改变者
    private List<ConditionChanger> changers = new ArrayList<>();
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
        judges = in.createTypedArrayList(ConditionJudge.CREATOR);
        changers = in.createTypedArrayList(ConditionChanger.CREATOR);
        in.readList(sons,Integer.class.getClassLoader());
        in.readList(fathers,Integer.class.getClassLoader());
        lastNodeIndex = in.readInt();
        index = in.readInt();
        Id = in.readLong();
        nodeName = in.readString();
        btnText = in.readString();
        plot = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(judges);
        dest.writeTypedList(changers);
        dest.writeList(sons);
        dest.writeList(fathers);
        dest.writeInt(lastNodeIndex);
        dest.writeInt(index);
        dest.writeLong(Id);
        dest.writeString(nodeName);
        dest.writeString(btnText);
        dest.writeString(plot);
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

    public List<ConditionJudge> getJudges() {
        return judges;
    }

    public List<ConditionChanger> getChangers() {
        return changers;
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
        sons.add(sonIndex);
    }

    public void addFather(int fatherIndex){
        fathers.add(fatherIndex);
    }

    public void toDefault(){
        nodeName  = DEFAULT_NAME;
        btnText = DEFAULT_BTN_TEXT;
        plot = DEFAULT_PLOT;
    }

    public void addJudge(@NonNull ConditionJudge judge){
        judges.add(judge);
    }
    public void removeJudge(@NonNull ConditionJudge judge){
        judges.remove(judge);
    }
    public void addChanger(@NonNull ConditionChanger changer){
        changers.add(changer);
    }
    public void removeChanger(@NonNull ConditionChanger changer){
        changers.remove(changer);
    }

    /**
     * 通过Condition获取判断
     * @param condition Judge持有的Condition
     * @return 找到返回Judge 找不到返回null
     */
    public ConditionJudge findJudgeByCondition(@NonNull Condition condition){
        for(int i=0;i<judges.size();i++){
            if(judges.get(i).getCondition().equals(condition))
                return judges.get(i);
        }
        return null;
    }

    /**
     * 根据Condition获取修改
     * @param condition Changer持有的Condition
     * @return 找到返回Changer 找不到返回null
     */
    public ConditionChanger findChangerByCondition(@NonNull Condition condition){
        for(int i=0;i<changers.size();i++){
            if(changers.get(i).getCondition().equals(condition))
                return changers.get(i);
        }
        return null;
    }
    public boolean judgeNode(){
        for(ConditionJudge judge:judges){
            if(!judge.judgeNode())
                return false;
        }
        return true;
    }

}
