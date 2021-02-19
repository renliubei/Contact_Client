package com.example.contact_client.interactive_creator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityInteracitveCreatorBinding;
import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoProject;

import java.util.List;

public class InteractiveCreatorActivity extends AppCompatActivity {
    //保存互动视频
    private VideoProject videoProject;
    //当前activity的binding
    ActivityInteracitveCreatorBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_interacitve_creator);
        mBinding.setLifecycleOwner(this);
        videoProject = new VideoProject();
    }

}