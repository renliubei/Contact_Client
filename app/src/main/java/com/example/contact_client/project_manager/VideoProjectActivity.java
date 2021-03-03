package com.example.contact_client.project_manager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.project_manager.PageTransformer.ZoomOutPageTransformer;
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
        modifyViewPager2();
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

    void modifyViewPager2(){
        adapter = new VideoProjectViewPagerAdapter();
        ViewPager2 viewPager2 = binding.viewPagerVideoProject;
        viewPager2.setAdapter(adapter);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new ZoomOutPageTransformer());
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setOffscreenPageLimit(1);
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        recyclerView.setPadding(20,0,20,0);
        recyclerView.setClipToPadding(false);
    }
}