package com.example.contact_client.project_creator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.project_creator.Condition.Condition;
import com.example.contact_client.project_creator.Condition.ConditionChanger;
import com.example.contact_client.project_creator.Condition.ConditionJudge;
import com.example.contact_client.project_creator.VideoNode;

import java.util.ArrayList;
import java.util.List;

public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.MyViewHolder>{
    private static List<Condition> conditionList;
    private VideoNode currentNode;
    private Context context;
    static{
        conditionList = new ArrayList<>();
        Condition condition1 = new Condition("test1",50,100,0);
        Condition condition2 = new Condition("test2",50,100,0);
        Condition condition3 = new Condition("test3",50,100,0);
        conditionList.add(condition1);conditionList.add(condition2);conditionList.add(condition3);
    }

    public ConditionAdapter(VideoNode currentNode,Context context) {
        this.currentNode = currentNode;
        this.context = context;
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
        Condition condition = conditionList.get(position);
        holder.textViewName.setText(condition.getConditionName());
        holder.asJudge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("mylo",currentNode.getJudges().toString());
                if(b){
                    if(!holder.editTextJudge.getEditableText().toString().isEmpty()){
                        holder.editTextJudge.setEnabled(false);
                        ConditionJudge judge = new ConditionJudge(condition,ConditionJudge.OVER,Integer.parseInt(holder.editTextJudge.getEditableText().toString().trim()));
                        currentNode.addJudge(judge);
                        holder.textViewJudge.setTextColor(Color.BLUE);
                    }
                    else{
                        compoundButton.setChecked(false);
                        Toast.makeText(compoundButton.getContext(),"请输入数字",Toast.LENGTH_SHORT).show();
                    }
                }else{
                        ConditionJudge conditionJudge = currentNode.findJudgeByCondition(condition);
                        currentNode.removeJudge(conditionJudge);
                        holder.editTextJudge.setEnabled(true);
                        holder.textViewJudge.setTextColor(Color.GRAY);
                }
            }
        });
        holder.asChanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!holder.editTextChanger.getEditableText().toString().isEmpty()){
                        holder.editTextChanger.setEnabled(false);
                        ConditionChanger changer = new ConditionChanger(condition,Integer.parseInt(holder.editTextChanger.getEditableText().toString().trim()));
                        currentNode.addChanger(changer);
                        holder.textViewChanger.setTextColor(Color.BLUE);
                    } else{
                        compoundButton.setChecked(false);
                        Toast.makeText(compoundButton.getContext(),"请输入数字",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ConditionChanger conditionChanger = currentNode.findChangerByCondition(condition);
                    currentNode.removeChanger(conditionChanger);
                    holder.editTextChanger.setEnabled(true);
                    holder.textViewChanger.setTextColor(Color.GRAY);
                }
            }
        });
        if(currentNode.findChangerByCondition(condition)!=null)
            holder.asChanger.setChecked(true);
        else
            holder.asChanger.setChecked(false);
        if(currentNode.findJudgeByCondition(condition)!=null)
            holder.asJudge.setChecked(true);
        else
            holder.asJudge.setChecked(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewChanger,textViewJudge;
        EditText editTextChanger, editTextJudge;
        Switch asJudge,asChanger;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextChanger = itemView.findViewById(R.id.editTextNumberConditionChanger);
            editTextJudge = itemView.findViewById(R.id.editTextNumberConditionJudge);
            textViewJudge = itemView.findViewById(R.id.textViewConditionAsJudge);
            textViewChanger = itemView.findViewById(R.id.textViewConditionAsChanger);
            textViewName = itemView.findViewById(R.id.conditionName);
            asChanger = itemView.findViewById(R.id.switchConditionAsChanger);
            asJudge = itemView.findViewById(R.id.switchConditionAsJudge);
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
