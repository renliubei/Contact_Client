package com.example.contact_client.project_creator.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchVideoCutAdapter extends RecyclerView.Adapter<SearchVideoCutAdapter.MyViewHolder> {

    private Map<Integer,Boolean> checkStatus = new HashMap<>();

    private List<VideoCut> allVideoCuts = new ArrayList<>();

    public Map<Integer, Boolean> getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Map<Integer, Boolean> checkStatus) {
        this.checkStatus = checkStatus;
    }

    public List<VideoCut> getAllVideoCuts() {
        return allVideoCuts;
    }

    public void setAllVideoCuts(List<VideoCut> allVideoCuts) {
        this.allVideoCuts = allVideoCuts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_normal_choose, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return allVideoCuts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoCut videoCut = allVideoCuts.get(position);
        Glide.with(holder.itemView)
                .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(holder.imageView);
        holder.textViewName.setText(videoCut.getName());
        holder.textViewDescription.setText(videoCut.getDescription());
        if(!checkStatus.containsKey(position)){ checkStatus.put(position,false);}
        try {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(checkStatus.get(position));
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStatus.put(position,isChecked);
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.performClick();
            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        ImageView imageView;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.VideoCutIcon);
            textViewName = itemView.findViewById(R.id.conditionName);
            textViewDescription = itemView.findViewById(R.id.videoNodeCutNameUnfold);
            checkBox = itemView.findViewById(R.id.checkBoxNodeDecided);
        }
    }
}
