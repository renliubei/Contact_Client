package com.example.contact_client.video_manager;

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
import com.example.contact_client.VideoCut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoCutsAdapter extends RecyclerView.Adapter<VideoCutsAdapter.MyViewHolder> {
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
        View itemview = layoutInflater.inflate(R.layout.cell_cardview, parent, false);
        return new MyViewHolder(itemview);
    }

    private onClickItem onClickItem;

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
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItem != null) {
                    onClickItem.onClickEdit(v, position);
                }
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItem != null) {
                    onClickItem.onClickDelete(v, position);
                }
            }
        });
    }

    public void setOnClickItem(onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public interface onClickItem {
        void onClickDelete(View v, int position);

        void onClickEdit(View v, int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        ImageView imageView, imageViewEdit, imageViewDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnailOfCardView);
            textViewName = itemView.findViewById(R.id.sonName);
            textViewDescription = itemView.findViewById(R.id.sonDesc);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
