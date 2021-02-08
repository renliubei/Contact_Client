package com.example.contact_client.video_manager;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoCut videoCut = allVideoCuts.get(position);
        Log.d("mylo", videoCut.toString());
        try {
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                    .placeholder(R.drawable.ic_baseline_face_24)
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
        Button checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.VideoThumbnail);
            checkbox = itemView.findViewById(R.id.videoCheckbutton);
        }
    }
}
