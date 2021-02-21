package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityInteracitveCreatorBinding;
import com.example.contact_client.repository.VideoCut;

import java.io.File;
import java.util.List;

public class InteractiveCreatorActivity extends AppCompatActivity {
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
        sonVideoCutsAdapter = new SonVideoCutsAdapter(mViewModel.getSonVideoCuts());
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
                        sonVideoCutsAdapter.removeData(position);
                    }

                    @Override
                    public void onClick(View v, int position) {
                        //更新父亲节点
                        VideoCut videoCut = sonVideoCutsAdapter.getAllVideoCuts().get(position);
                        updateFatherVideoCut(videoCut);
                        saveNodes(position);
                        //保存子节点
                        //清空列表
                        sonVideoCutsAdapter.clearData();
                    }
                });
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
                        //添加数据
                        sonVideoCutsAdapter.insertData(list);
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    void updateFatherVideoCut(VideoCut videoCut){
        mBinding.fatherName.setText(videoCut.getName());
        mBinding.fatherDescription.setText(videoCut.getDescription());
        Glide.with(this)
                .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(mBinding.fatherIcon);
    }

    void saveNodes(int position){
        //储存节点！
        try {
            VideoNode currentNode = mViewModel.getVideoNode();
            for(int i=0;i<mViewModel.getSonVideoCuts().size();i++){
                VideoNode videoNode = new VideoNode(mViewModel.getVideoNode().getCurrentIndex(),mViewModel.getVideoProject().getListSize(),mViewModel.getSonVideoCuts().get(i).getId());
                currentNode.addSons(mViewModel.getVideoProject().getListSize());
                mViewModel.getVideoProject().addNode(videoNode);
                if(i==position){
                    mViewModel.setVideoNode(videoNode);
                }
            }
            Log.d("mylo",mViewModel.getVideoProject().getVideoNodeList().toString());
            Log.d("mylo",currentNode.getSons().toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
        }
    }
}