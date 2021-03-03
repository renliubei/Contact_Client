package com.example.contact_client.project_manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.repository.VideoProject;

import java.util.ArrayList;
import java.util.List;

public class VideoProjectViewPagerAdapter extends RecyclerView.Adapter<VideoProjectViewPagerAdapter.mViewHolder> {
    List<VideoProject> data = new ArrayList<>();

    public List<VideoProject> getData() {
        return data;
    }

    public void setData(List<VideoProject> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_viewpager_videoproject, parent, false);
        return new VideoProjectViewPagerAdapter.mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        VideoProject videoProject = data.get(position);
        holder.name.setText(videoProject.getName()+videoProject.getId());
        holder.description.setText(videoProject.getDescription());
        Glide.with(holder.itemView)
                .load(R.drawable.ic_baseline_home_48)
                .into(holder.thumbNail);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
    ImageView thumbNail;
    TextView name,description;
    public mViewHolder(@NonNull View itemView) {
        super(itemView);
        thumbNail = itemView.findViewById(R.id.imageViewProjectThumbnail);
        name = itemView.findViewById(R.id.textViewProjectName);
        description = itemView.findViewById(R.id.textViewProjectDesc);
    }
}

}
