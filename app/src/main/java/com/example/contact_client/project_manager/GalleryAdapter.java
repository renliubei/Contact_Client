package com.example.contact_client.project_manager;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.repository.VideoProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.mViewHolder>{

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
        View itemView = inflater.inflate(R.layout.cell_cardview_videoproject,parent,false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        VideoProject videoProject = videoProjects.get(position);
        if (videoProject.getCoverUrl() == null) {
            Glide.with(holder.itemView)
                    .load(R.drawable.ic_baseline_home_48)
                    .into(holder.imageView);
        } else {
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(new File(videoProject.getCoverUrl())))
                    .error(R.drawable.ic_baseline_home_48)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return videoProjects.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProjectThumbnail);
        }
    }
}
