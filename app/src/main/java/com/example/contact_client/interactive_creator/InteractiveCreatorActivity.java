package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityInteracitveCreatorBinding;
import com.example.contact_client.repository.VideoProject;

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

        mBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchRoomForVideoCutActivity.class);
                startActivity(intent);
            }
        });
    }

}