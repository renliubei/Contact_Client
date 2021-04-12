package com.example.contact_client.project_creator;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoProject;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    CreatorViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewModel = new ViewModelProvider(this).get(CreatorViewModel.class);
        bindDataToViewModel(getIntent().getParcelableExtra(getString(R.string.videoProject)));
    }

    void bindDataToViewModel(VideoProject videoProject){
        if(mViewModel==null){
            Toasty.error(getApplication(),"绑定数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if(videoProject==null){
            Calendar calendar = Calendar.getInstance();
            videoProject = new VideoProject("互动视频","创建于"+calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"\t"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
            VideoNode videoNode = new VideoNode(-1,0,-1);
            videoProject.addNode(videoNode);
        }
        mViewModel.setVideoProject(videoProject);
        mViewModel.setCurrentNode(0);
    }
}