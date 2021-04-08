package com.example.contact_client.project_creator;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;
import com.ramotion.foldingcell.FoldingCell;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SonVideoCutsAdapter extends RecyclerView.Adapter<SonVideoCutsAdapter.MyViewHolder> {

    private final Transformation<Bitmap> circleCrop = new CircleCrop();
    private List<VideoCut> allVideoCuts;
    private final List<VideoNode> nodes;
    private VideoNode currentNode;

    public List<VideoCut> getAllVideoCuts() {
        return allVideoCuts;
    }

    public void setAllVideoCuts(List<VideoCut> allVideoCuts) {
        this.allVideoCuts = allVideoCuts;
        notifyDataSetChanged();
    }


    public void setCurrentNode(VideoNode currentNode) {
        this.currentNode = currentNode;
    }

    public SonVideoCutsAdapter(List<VideoCut> allVideoCuts, List<VideoNode> nodes, VideoNode currentNode) {
        this.allVideoCuts = allVideoCuts;
        this.nodes = nodes;
        this.currentNode = currentNode;
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
        VideoNode videoNode = nodes.get(currentNode.getSons().get(position));
        //图片
        if(videoCut.getId()==-1){
            Glide.with(holder.itemView)
                    .load(R.drawable.ic_baseline_home_48)
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .into(holder.imageView);
            Glide.with(holder.itemView)
                    .load(R.drawable.defualt_project_cover)
                    .into(holder.head_image);
        }else{
            File file = new File(videoCut.getThumbnailPath());
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(file))
                    .apply(RequestOptions.bitmapTransform(circleCrop))
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .into(holder.imageView);
            Glide.with(holder.itemView)
                    .load(Uri.fromFile(file))
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .into(holder.head_image);
        }
        //读入结点信息
        holder.textViewNodeName.setText(videoNode.getNodeName());
        holder.textViewNodeNameUnfold.setText(videoNode.getNodeName());
        holder.textViewPlot.setText(videoNode.getPlot());
        holder.textViewBtnText.setText(videoNode.getBtnText());
        holder.textViewOrder.setText(String.valueOf(position+1));
        holder.textViewSons.setText(videoNode.getSons().toString());
        //读入视频信息
        holder.textViewCutName.setText(videoCut.getName());
        holder.textViewCutNameUnfold.setText(videoCut.getName());
        holder.textViewCutDesc.setText(videoCut.getDescription());
        //注册按键事件
        holder.imageViewChange.setOnClickListener(v->{
            if (onClickItem != null) {
                onClickItem.onClickChangeNode(v, position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            holder.foldingCell.toggle(false);
        });

        holder.imageViewDelete.setOnClickListener(v -> {
            if (onClickItem != null) {
                onClickItem.onClickDeleteNode(v, position);
            }
        });

        holder.enter.setOnClickListener(v -> {
            holder.foldingCell.toggle(false);
            if(onClickItem != null){
                onClickItem.onClick(v,position);
            }
        });

        holder.save.setOnClickListener(v -> {
            if(!holder.textViewNodeNameUnfold.getText().toString().isEmpty()){
                videoNode.setNodeName(holder.textViewNodeNameUnfold.getText().toString());
                holder.textViewNodeName.setText(videoNode.getNodeName());
            }else{
                Toasty.error(v.getContext(),"名称不能为空",Toasty.LENGTH_SHORT).show();
            }
            if(!holder.textViewBtnText.getText().toString().isEmpty()){
                videoNode.setBtnText(holder.textViewBtnText.getText().toString());
            }else{
                Toasty.error(v.getContext(),"选项内容不能为空",Toasty.LENGTH_SHORT).show();
            }
            if(!holder.textViewPlot.getText().toString().isEmpty()){
                videoNode.setPlot(holder.textViewPlot.getText().toString());
            }
            Toast.makeText(v.getContext(),"保存成功",Toast.LENGTH_SHORT).show();
        });
    }

    private onClickItem onClickItem;
    public void setOnClickItem(onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }
    public interface onClickItem {
        void onClickDeleteNode(View v, int position);
        void onClickChangeNode(View v, int position);
        void onClick(View v,int position);
    }

    public void removeData(int position){
        allVideoCuts.remove(position);
        notifyItemRemoved(position);
        if (position != getItemCount()) {
           notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    public void insertData(List<VideoCut> videoCuts){
        int start = getItemCount();
        allVideoCuts.addAll(videoCuts);
        notifyItemRangeInserted(start,videoCuts.size());
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNodeNameUnfold, textViewCutNameUnfold,textViewPlot,textViewBtnText,textViewSons,textViewCutDesc;
        TextView textViewNodeName,textViewOrder, textViewCutName;
        ImageView imageView,imageViewDelete, imageViewChange,head_image;
        Button enter,save;
        FoldingCell foldingCell;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCutDesc = itemView.findViewById(R.id.videoNodeCutDesc);
            imageView = itemView.findViewById(R.id.thumbnailOfCardView);
            textViewNodeName = itemView.findViewById(R.id.videoNodeName);
            textViewCutName = itemView.findViewById(R.id.videoNodeCutName);
            textViewNodeNameUnfold = itemView.findViewById(R.id.videoNodeNameUnfold);
            textViewCutNameUnfold = itemView.findViewById(R.id.videoNodeCutNameUnfold);
            imageViewChange = itemView.findViewById(R.id.imageViewChange);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            head_image = itemView.findViewById(R.id.head_image);
            textViewPlot = itemView.findViewById(R.id.videoNodePlot);
            textViewBtnText = itemView.findViewById(R.id.videoNodeBtnText);
            textViewSons = itemView.findViewById(R.id.textViewSons);
            textViewOrder = itemView.findViewById(R.id.textViewOrder);
            save = itemView.findViewById(R.id.buttonSaveNodeContent);
            enter = itemView.findViewById(R.id.buttonEnterNode);
            foldingCell = itemView.findViewById(R.id.foldingCell);
        }
    }
}
