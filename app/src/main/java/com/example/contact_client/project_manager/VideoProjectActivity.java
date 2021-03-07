package com.example.contact_client.project_manager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.interactive_creator.InteractiveCreatorActivity;
import com.example.contact_client.repository.VideoProject;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class VideoProjectActivity extends AppCompatActivity {
    ProjectViewModel mViewModel;
    ActivityVideoProjectBinding binding;
    GalleryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_project);
        binding.setLifecycleOwner(this);
        //
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        //
        modifyRecyclerView();
        //
        registerButtonEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getProjectsLiveDataList().observe(this, videoProjects -> {
            Log.d("mylo","videoProject 0 are: "+videoProjects.get(0).toString());
            adapter.setVideoProjects(videoProjects);
            adapter.notifyDataSetChanged();
        });
    }

    void modifyRecyclerView(){
        adapter = new GalleryAdapter();
        //设置adapter动画
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setDuration(2000);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        scaleInAnimationAdapter.setFirstOnly(false);
        //绑定adapter和layoutManager
        binding.recyclerViewProjects.setAdapter(scaleInAnimationAdapter);
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
                        mViewModel.setPosition(position);
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

    void registerButtonEvents(){
        binding.btnEditProject.setOnClickListener(v -> startProjectCreator());
        binding.btnDisplayProject.setOnClickListener(v -> Toast.makeText(v.getContext(),"尚未开发",Toast.LENGTH_SHORT).show());
    }

    void startProjectCreator(){
        if(mViewModel.getPosition()==-1){
            Toast.makeText(this,"无互动视频 or 请先滑动选中",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, InteractiveCreatorActivity.class);
            intent.putExtra(getString(R.string.videoProject),adapter.getVideoProjects().get(mViewModel.getPosition()).getId());
            startActivity(intent);
        }
    }

    void startProjectEditor(){
        Intent intent = new Intent(this, takePhotoActivity.class);
        startActivity(intent);
    }
}