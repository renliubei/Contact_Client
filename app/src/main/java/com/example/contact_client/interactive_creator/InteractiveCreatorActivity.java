package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
    //绑定ViewModel
    CreatorViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_interacitve_creator);
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(CreatorViewModel.class);
        mBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchRoomForVideoCutActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getSonVideoCuts().observe(this, new Observer<List<VideoCut>>() {
            @Override
            public void onChanged(List<VideoCut> videoCuts) {
                Log.d("mylo","List Update : "+mViewModel.getSonVideoCuts().getValue().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                        Log.d("mylo","Receive Data from Room : "+list.toString());
                        for(int i=0;i<list.size();i++){
                            mViewModel.getSonVideoCuts().getValue().add(list.get(i));
                        }
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}