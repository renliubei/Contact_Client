package com.example.contact_client.project_manager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;

public class VideoProjectActivity extends AppCompatActivity {
    private ProjectViewModel mViewModel;
    private ActivityVideoProjectBinding binding;
    private GalleryAdapter galleryAdapter;
    private BottomTextAdapter bottomTextAdapter;
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
        }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getProjectsLiveDataList().observe(this, videoProjects -> {

            galleryAdapter.setVideoProjects(videoProjects);
            galleryAdapter.notifyDataSetChanged();

            bottomTextAdapter.setVideoProjects(videoProjects);
            bottomTextAdapter.notifyDataSetChanged();
        });
    }
    void modifyRecyclerView(){
        modifyProjectsRecyclerView();
        modifyBottomRecyclerView();
    }

    void modifyBottomRecyclerView(){
        bottomTextAdapter = new BottomTextAdapter();
        SlideInLeftAnimationAdapter slideInLeftAnimationAdapter = new SlideInLeftAnimationAdapter(bottomTextAdapter);
        slideInLeftAnimationAdapter.setDuration(1000);
        binding.recyclerViewBottomText.setLayoutManager(new ScrollSpeedLinearLayoutManger(this));
        binding.recyclerViewBottomText.setAdapter(slideInLeftAnimationAdapter);
        binding.recyclerViewBottomText.post(() -> bottomTextAdapter.setOnClickItem(new BottomTextAdapter.onClickItem() {
            @Override
            public void onClickDelete(View v, int position) {
                Toast.makeText(v.getContext(),"you click delete",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickDisplay(View v, int position) {
                Toast.makeText(v.getContext(),"you click display",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickEdit(View v, int position) {
                mViewModel.setPosition(position);
                startProjectCreator();
            }
        }));
    }

    void modifyProjectsRecyclerView(){
        galleryAdapter = new GalleryAdapter();
        //设置adapter动画
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(galleryAdapter);
        animationAdapter.setDuration(1500);
        animationAdapter.setInterpolator(new OvershootInterpolator());
        animationAdapter.setFirstOnly(false);
        //绑定adapter和layoutManager
        binding.recyclerViewProjects.setAdapter(animationAdapter);
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
                        binding.recyclerViewBottomText.smoothScrollToPosition(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    void startProjectCreator(){
        if(mViewModel.getPosition()==-1){
            Toast.makeText(this,"无互动视频 or 请先滑动选中",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, InteractiveCreatorActivity.class);
            intent.putExtra(getString(R.string.videoProject), galleryAdapter.getVideoProjects().get(mViewModel.getPosition()).getId());
            startActivity(intent);
        }
    }

    void startProjectEditor(){
        Intent intent = new Intent(this, takePhotoActivity.class);
        startActivity(intent);
    }

    class disScrollLinearLayoutManager extends LinearLayoutManager{
        private boolean isScrollEnabled = true;
        public disScrollLinearLayoutManager(Context context) {
            super(context);
        }
        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }
        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}