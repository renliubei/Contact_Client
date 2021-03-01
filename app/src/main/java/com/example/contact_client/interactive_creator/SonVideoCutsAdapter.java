package com.example.contact_client.interactive_creator;

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
import com.example.contact_client.repository.VideoCut;

import java.io.File;
import java.util.List;

public class SonVideoCutsAdapter extends RecyclerView.Adapter<SonVideoCutsAdapter.MyViewHolder> {


    private List<VideoCut> allVideoCuts;

    public List<VideoCut> getAllVideoCuts() {
        return allVideoCuts;
    }

    public void setAllVideoCuts(List<VideoCut> allVideoCuts) {
        this.allVideoCuts = allVideoCuts;
        notifyDataSetChanged();
    }

    public SonVideoCutsAdapter(List<VideoCut> allVideoCuts) {
        this.allVideoCuts = allVideoCuts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_cardview_son, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return allVideoCuts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoCut videoCut = allVideoCuts.get(position);
        if(videoCut.getId()==-1){
            Glide.with(holder.itemView)
                    .load(R.drawable.ic_baseline_home_48)
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .into(holder.imageView);
        }else{
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .into(holder.imageView);
        }
        holder.textViewName.setText(videoCut.getName());
        holder.textViewDescription.setText(videoCut.getDescription());
        holder.textViewOrder.setText(String.valueOf(position+1));
        holder.imageViewEdit.setOnClickListener(v->{
            if (onClickItem != null) {
                onClickItem.onClickEdit(v, position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onClickItem != null) {
                onClickItem.onClick(v, position);
            }
        });
        holder.imageViewDelete.setOnClickListener(v -> {
            if (onClickItem != null) {
                onClickItem.onClickDelete(v, position);
            }
        });
    }

    private onClickItem onClickItem;

    public void setOnClickItem(onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public interface onClickItem {
        void onClickDelete(View v, int position);
        void onClickEdit(View v,int position);
        void onClick(View v,int position);
    }

    public void removeData(int position){
        allVideoCuts.remove(position);
        notifyItemRemoved(position);
        if (position != getItemCount()) {
           notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    public void clearData(){
        allVideoCuts.clear();
        notifyDataSetChanged();
    }

    public void insertData(List<VideoCut> videoCuts){
        int start = getItemCount();
        allVideoCuts.addAll(videoCuts);
        notifyItemRangeInserted(start,videoCuts.size());
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription,textViewOrder;
        ImageView imageView,imageViewDelete,imageViewEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnailOfCardView);
            textViewName = itemView.findViewById(R.id.videoNodeName);
            textViewDescription = itemView.findViewById(R.id.videoNodeSons);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            textViewOrder = itemView.findViewById(R.id.textViewOrder);
        }
    }
}
