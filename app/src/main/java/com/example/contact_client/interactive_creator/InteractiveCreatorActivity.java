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
import androidx.recyclerview.widget.LinearLayoutManager;

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
    //
    SonVideoCutsAdapter sonVideoCutsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_interacitve_creator);
        mBinding.setLifecycleOwner(this);
        //
        mViewModel = new ViewModelProvider(this).get(CreatorViewModel.class);
        //
        videoProject = new VideoProject();
        //
        sonVideoCutsAdapter = new SonVideoCutsAdapter();
        mBinding.recyclerView.setAdapter(sonVideoCutsAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //注册按键功能
        mBinding.buttonAdd.setOnClickListener(v-> startActivityForResult(new Intent(v.getContext(), SearchRoomForVideoCutActivity.class),1));
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                sonVideoCutsAdapter.setOnClickItem(new SonVideoCutsAdapter.onClickItem() {
                    @Override
                    public void onClickDelete(View v, int position) {
                        //删除
                        mViewModel.getSonVideoCuts().getValue().remove(position);
                        sonVideoCutsAdapter.setAllVideoCuts(mViewModel.getSonVideoCuts().getValue());
                        sonVideoCutsAdapter.notifyDataSetChanged();
                        Log.d("mylo","List Deleted : "+mViewModel.getSonVideoCuts().getValue().toString());
                    }

                    @Override
                    public void onClick(View v, int position) {
                        mViewModel.getSonVideoCuts().getValue().clear();
                        sonVideoCutsAdapter.setAllVideoCuts(mViewModel.getSonVideoCuts().getValue());
                        sonVideoCutsAdapter.notifyDataSetChanged();
                        Log.d("mylo","List Deleted : "+mViewModel.getSonVideoCuts().getValue().toString());
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getSonVideoCuts().observe(this, new Observer<List<VideoCut>>() {
            @Override
            public void onChanged(List<VideoCut> videoCuts) {
                //为什么在删除的时候没有反馈？
                Log.d("mylo","List Update : "+mViewModel.getSonVideoCuts().getValue().toString());
                sonVideoCutsAdapter.setAllVideoCuts(videoCuts);
                sonVideoCutsAdapter.notifyDataSetChanged();
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
                        //添加从数据库中获取的data
                        List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                        Log.d("mylo","Receive Data from Room : "+list.toString());
                        mViewModel.getSonVideoCuts().getValue().addAll(list);
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}