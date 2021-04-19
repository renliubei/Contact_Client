package com.example.contact_client.project_creator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.project_creator.Condition.Condition;
import com.example.contact_client.project_creator.Condition.ConditionChanger;
import com.example.contact_client.project_creator.Condition.ConditionJudge;
import com.example.contact_client.project_creator.VideoNode;

import java.util.HashMap;
import java.util.List;

public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.MyViewHolder>{
    private List<Condition> conditionList;
    private  VideoNode currentNode;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private final HashMap<Integer,Integer> hashMapJudge;
    private final HashMap<Integer,Integer> hashMapChanger;
    private final HashMap<Integer,Integer> hashMapSpinner;
    private static final int yellow = Color.argb(100,242,166,82);
    private static final int green = Color.argb(100,117,185,110);
    private static final int red = Color.argb(100,233,106,53);
    private static final int purple = Color.argb(100,119,84,38);
    private static final int blue = Color.argb(100,66,193,226);
    private final int[] colors = new int[]{yellow,green,purple,red,blue};

    public ConditionAdapter(VideoNode currentNode, List<Condition> conditions, Context context) {
        this.currentNode = currentNode;
        conditionList = conditions;
        hashMapChanger = new HashMap<>();
        hashMapJudge = new HashMap<>();
        hashMapSpinner=new HashMap<>();
        initSpinner(context);
        buildCondition();
    }
    public void changeNode(VideoNode node){
        currentNode=node;
        buildCondition();
        notifyDataSetChanged();
    }
    private void initSpinner(Context context){
        spinnerAdapter = ArrayAdapter.createFromResource(context,R.array.judges_array,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    /**
     * 根据当前节点获取条件器情况
     */
    public void buildCondition(){
        ConditionJudge judge;
        ConditionChanger changer;
        hashMapChanger.clear();
        hashMapJudge.clear();
        hashMapSpinner.clear();
        for(int i=0;i<conditionList.size();i++){
            if((judge=currentNode.findJudgeByCondition(conditionList.get(i)))!=null){
                Log.d("mylo","add judge");
                hashMapJudge.put(i,judge.getRequiredValue());
                hashMapSpinner.put(i,judge.getJudgeWay());
            }
            if((changer=currentNode.findChangerByCondition(conditionList.get(i)))!=null){
                Log.d("mylo","add change");
                hashMapChanger.put(i,changer.getChange());
            }
        }
    }
    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cell_cardview_condition,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int[] judgeWay = new int[]{0};
        Condition condition = conditionList.get(position);
        holder.constraintLayout.setBackgroundColor(colors[position%5]);
        holder.textViewName.setText(condition.getConditionName());
        holder.spinnerJudge.setAdapter(spinnerAdapter);
        holder.spinnerJudge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                judgeWay[0] =position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(),"请选择方式",Toast.LENGTH_SHORT).show();
            }
        });
        holder.asJudge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("mylo",currentNode.getJudges().toString());
                if(b){
                    if(!holder.editTextJudge.getEditableText().toString().isEmpty()){
                        holder.editTextJudge.setEnabled(false);
                        holder.spinnerJudge.setEnabled(false);
                        int value = Integer.parseInt(holder.editTextJudge.getEditableText().toString().trim());
                        ConditionJudge judge = new ConditionJudge(condition,judgeWay[0],value);
                        currentNode.addJudge(judge);
                        hashMapJudge.put(position,value);
                        hashMapSpinner.put(position,judgeWay[0]);
                    }
                    else{
                        compoundButton.setChecked(false);
                        Toast.makeText(compoundButton.getContext(),"请输入数字",Toast.LENGTH_SHORT).show();
                    }
                }else{
                        ConditionJudge conditionJudge = currentNode.findJudgeByCondition(condition);
                        currentNode.removeJudge(conditionJudge);
                        hashMapJudge.remove(position);
                        hashMapSpinner.remove(position);
                        holder.editTextJudge.setEnabled(true);
                        holder.spinnerJudge.setEnabled(true);
                }
            }
        });
        holder.asChanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!holder.editTextChanger.getEditableText().toString().isEmpty()){
                        holder.editTextChanger.setEnabled(false);
                        holder.textViewChanger.setTextColor(Color.GRAY);
                        int value = Integer.parseInt(holder.editTextChanger.getEditableText().toString().trim());
                        ConditionChanger changer = new ConditionChanger(condition,value);
                        currentNode.addChanger(changer);
                        hashMapChanger.put(position,value);
                    } else{
                        compoundButton.setChecked(false);
                        Toast.makeText(compoundButton.getContext(),"请输入数字",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ConditionChanger conditionChanger = currentNode.findChangerByCondition(condition);
                    currentNode.removeChanger(conditionChanger);
                    hashMapChanger.remove(position);
                    holder.editTextChanger.setEnabled(true);
                    holder.textViewChanger.setTextColor(Color.BLACK);
                }
            }
        });
        if(hashMapJudge.containsKey(position)&&hashMapSpinner.containsKey(position)){
            holder.editTextJudge.getEditableText().clear();
            holder.editTextJudge.getEditableText().append(String.valueOf(hashMapJudge.get(position)));
            holder.asJudge.setChecked(true);
            holder.spinnerJudge.setSelection(hashMapSpinner.get(position),true);
        }else{
            holder.editTextJudge.getEditableText().clear();
            holder.asJudge.setChecked(false);
        }
        if(hashMapChanger.containsKey(position)){
            holder.editTextChanger.getEditableText().clear();
            holder.editTextChanger.getEditableText().append(String.valueOf(hashMapChanger.get(position)));
            holder.asChanger.setChecked(true);
        }else{
            holder.editTextChanger.getEditableText().clear();
            holder.asChanger.setChecked(false);
        }
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClick!=null)
                    onClick.onClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conditionList.size();
    }

    public void addCondition(Condition condition) {
        int pos = getItemCount();
        conditionList.add(condition);
        notifyItemInserted(pos);
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewChanger;
        Spinner spinnerJudge;
        EditText editTextChanger, editTextJudge;
        Switch asJudge,asChanger;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextChanger = itemView.findViewById(R.id.editTextNumberConditionChanger);
            editTextJudge = itemView.findViewById(R.id.editTextNumberConditionJudge);
            spinnerJudge = itemView.findViewById(R.id.spinnerJudgeConditions);
            textViewChanger = itemView.findViewById(R.id.textViewConditionAsChanger);
            textViewName = itemView.findViewById(R.id.conditionName);
            asChanger = itemView.findViewById(R.id.switchConditionAsChanger);
            asJudge = itemView.findViewById(R.id.switchConditionAsJudge);
            constraintLayout=itemView.findViewById(R.id.ViewCondition);
        }
    }
    public interface onClick{
        public void onClick(View v,int position);
    }

    private onClick onClick;

    public void setOnClick(ConditionAdapter.onClick onClick) {
        this.onClick = onClick;
    }
}
