package com.example.contact_client.interactive_creator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchVideoNodeAdapter extends RecyclerView.Adapter<SearchVideoNodeAdapter.MyViewHolder> {

    private Map<Integer,Boolean> checkStatus = new HashMap<>();

    private List<VideoNode> videoNodeList;

    public Map<Integer, Boolean> getCheckStatus() {
        return checkStatus;
    }


    public List<VideoNode> getVideoNodeList() {
        return videoNodeList;
    }

    public void setVideoNodeList(List<VideoNode> videoNodeList) {
        this.videoNodeList = videoNodeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_normal_choose_videonode, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return videoNodeList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoNode videoNode = videoNodeList.get(position);
        holder.textViewName.setText(videoNode.getName());
        String sons = "sons: "+videoNode.getSons().toString();
        holder.textViewSons.setText(sons);
        String index = "P" + videoNode.getIndex();
        holder.textViewIndex.setText(index);
        if(!checkStatus.containsKey(position)){ checkStatus.put(position,false);}
        try {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(checkStatus.get(position));
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> checkStatus.put(position,isChecked));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> holder.checkBox.performClick());
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewSons,textViewIndex;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.videoNodeName);
            textViewSons = itemView.findViewById(R.id.videoNodeSons);
            textViewIndex = itemView.findViewById(R.id.videoNodeIndex);
            checkBox = itemView.findViewById(R.id.checkBoxNodeDecided);
        }
    }
}
