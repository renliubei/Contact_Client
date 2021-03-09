package com.example.contact_client.project_manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoProject;

import java.util.ArrayList;
import java.util.List;

public class BottomTextAdapter extends RecyclerView.Adapter<BottomTextAdapter.mViewHolder>{

    List<VideoProject> videoProjects = new ArrayList<>();

    public List<VideoProject> getVideoProjects() {
        return videoProjects;
    }

    public void setVideoProjects(List<VideoProject> videoProjects) {
        this.videoProjects = videoProjects;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cell_swipemenu_videoproject,parent,false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        VideoProject videoProject = videoProjects.get(position);
        holder.textViewDesc.setText(videoProject.getDescription());
        holder.textViewName.setText(videoProject.getName());
        holder.btnEdit.setOnClickListener(v -> {
            if(onClickItem!=null){
                onClickItem.onClickEdit(v,position);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            if(onClickItem!=null){
                onClickItem.onClickDelete(v,position);
            }
        });
        holder.btnDisplay.setOnClickListener(v -> {
            if(onClickItem!=null){
                onClickItem.onClickDisplay(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoProjects.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewDesc;
        Button btnEdit,btnDelete,btnDisplay;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewProjectName);
            textViewDesc = itemView.findViewById(R.id.textViewProjectDesc);
            btnEdit = itemView.findViewById(R.id.btnEditProject);
            btnDelete = itemView.findViewById(R.id.btnDeleteProject);
            btnDisplay = itemView.findViewById(R.id.btnDisplayProject);
        }
    }

    public interface onClickItem {
        void onClickDelete(View v, int position);
        void onClickDisplay(View v,int position);
        void onClickEdit(View v, int position);
    }

    private onClickItem onClickItem;

    public void setOnClickItem(BottomTextAdapter.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }
}