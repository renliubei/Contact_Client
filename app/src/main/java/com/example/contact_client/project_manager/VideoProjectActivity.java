package com.example.contact_client.project_manager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.repository.VideoProject;

import java.util.List;

public class VideoProjectActivity extends AppCompatActivity {
    ProjectViewModel mViewModel;
    ActivityVideoProjectBinding binding;
    VideoProjectViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_project);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        adapter = new VideoProjectViewPagerAdapter();
        binding.viewPagerVideoProject.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getProjectsLiveDataList().observe(this, new Observer<List<VideoProject>>() {
            @Override
            public void onChanged(List<VideoProject> videoProjects) {
                adapter.setData(videoProjects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}