package com.example.contact_client.project_manager;

import android.net.Uri;
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
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectGalleryAdapter extends RecyclerView.Adapter<ProjectGalleryAdapter.mViewHolder> {
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
        View itemView = layoutInflater.inflate(R.layout.cell_cardview_videoproject, parent, false);
        return new ProjectGalleryAdapter.mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        VideoProject videoProject = data.get(position);
        holder.name.setText(videoProject.getName()+videoProject.getId());
        holder.description.setText(videoProject.getDescription());
        holder.swipeMenuLayout.setIos(true);
        if(videoProject.getCoverUrl()==null){
            Glide.with(holder.itemView)
                    .load(R.drawable.ic_baseline_home_48)
                    .into(holder.thumbNail);
        }else{
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(new File(videoProject.getCoverUrl())))
                    .error(R.drawable.ic_baseline_home_48)
                    .into(holder.thumbNail);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
    ImageView thumbNail;
    TextView name,description;
    SwipeMenuLayout swipeMenuLayout;
    public mViewHolder(@NonNull View itemView) {
        super(itemView);
        thumbNail = itemView.findViewById(R.id.imageViewProjectThumbnail);
        name = itemView.findViewById(R.id.textViewProjectName);
        description = itemView.findViewById(R.id.textViewProjectDesc);
        swipeMenuLayout = itemView.findViewById(R.id.swipeMenu);
    }
}

}
