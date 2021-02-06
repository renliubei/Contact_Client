package com.example.contact_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoCut videoCut = allVideoCuts.get(position);
        holder.textViewId.setText(String.valueOf(videoCut.getId()));
        holder.textViewName.setText(videoCut.getName());
        holder.textViewDescription.setText(videoCut.getDescription());
    }

    @Override
    public int getItemCount() {
        return allVideoCuts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewName, textViewDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDesc);
        }
    }
}
