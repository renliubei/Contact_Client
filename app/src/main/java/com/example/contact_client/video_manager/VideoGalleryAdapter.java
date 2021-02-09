package com.example.contact_client.video_manager;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.VideoCut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.MyViewHolder> {
    List<VideoCut> allVideoCuts = new ArrayList<>();

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
        View itemView = layoutInflater.inflate(R.layout.gallery_cell, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.checkbox.isChecked()) {
                    allVideoCuts.get(myViewHolder.getAdapterPosition()).setCut(false);
                    myViewHolder.checkbox.setChecked(false);
                } else {
                    allVideoCuts.get(myViewHolder.getAdapterPosition()).setCut(true);
                    myViewHolder.checkbox.setChecked(true);
                }
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoCut videoCut = allVideoCuts.get(position);
        try {
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return allVideoCuts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.VideoThumbnail);
            checkbox = itemView.findViewById(R.id.videoCheckbutton);
        }
    }
}
