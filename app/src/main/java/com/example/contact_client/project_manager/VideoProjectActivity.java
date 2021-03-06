package com.example.contact_client.project_manager;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.repository.VideoProject;

import java.util.List;

public class VideoProjectActivity extends AppCompatActivity {
    ProjectViewModel mViewModel;
    ActivityVideoProjectBinding binding;
    GalleryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_project);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        modifyRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getProjectsLiveDataList().observe(this, new Observer<List<VideoProject>>() {
            @Override
            public void onChanged(List<VideoProject> videoProjects) {
                adapter.setVideoProjects(videoProjects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    void modifyRecyclerView(){
        //绑定adapter和layoutManager
        adapter = new GalleryAdapter();
        binding.recyclerViewProjects.setAdapter(adapter);
        binding.recyclerViewProjects.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //绑定snapHelper
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerViewProjects);
        //修改首尾item的margin
        binding.recyclerViewProjects.addItemDecoration(new MarginItemDecoration());
        //修改text的内容
        binding.recyclerViewProjects.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getChildCount() > 0 && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    try {
                        int position = recyclerView.getChildAdapterPosition(snapHelper.findSnapView(recyclerView.getLayoutManager()));
                        VideoProject videoProject = adapter.getVideoProjects().get(position);
                        setBottomText(videoProject.getName()+videoProject.getId(),videoProject.getDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void setBottomText(String name,String desc){
        binding.textViewProjectName.setText(name);
        binding.textViewProjectDesc.setText(desc);
    }
}