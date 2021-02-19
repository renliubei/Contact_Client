package com.example.contact_client.video_manager;

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
import com.example.contact_client.VideoCut;
import com.example.contact_client.databinding.ActivityVideoManagerBinding;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class VideoManagerActivity extends AppCompatActivity {
    //用于进行异步操作
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private VideoCutsViewModel videoCutsViewModel;

    private VideoCutsAdapter videoCutsAdapter;

    private VideoCut videoCutEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取数据库和binding
        ActivityVideoManagerBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_manager);
        binding.setLifecycleOwner(this);
        videoCutsViewModel = new ViewModelProvider(this).get(VideoCutsViewModel.class);

        //设置recycleView布局
        videoCutsAdapter = new VideoCutsAdapter();
        binding.videoCutsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.videoCutsRecycleView.setAdapter(videoCutsAdapter);
        binding.videoCutsRecycleView.post(new Runnable() {
            @Override
            public void run() {
                videoCutsAdapter.setOnClickItem(new VideoCutsAdapter.onClickItem() {
                    @Override
                    public void onClickDelete(View v, int position) {
                        DeleteVideoCuts(videoCutsAdapter.getAllVideoCuts().get(position));
                    }

                    @Override
                    public void onClickEdit(View v, int position) {
                        Toast.makeText(v.getContext(), "you click Editor", Toast.LENGTH_SHORT).show();
                        //使得editor指向需要更新的videoCut，并调用获取新名字和描述的activity，在回调中实现更新
                        videoCutEditor = videoCutsAdapter.getAllVideoCuts().get(position);
                        StartEditActivity();
                    }
                });
            }
        });
        //为button注册事件
        binding.buttonInsert.setOnClickListener(v -> StartGetVideoCutActivity());
        binding.buttonClear.setOnClickListener(v -> ClearVideoCuts());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //添加observer
        videoCutsViewModel.getAllLiveDataVideoCuts().observe(this, new Observer<List<VideoCut>>() {
            @Override
            public void onChanged(List<VideoCut> videoCuts) {
                videoCutsAdapter.setAllVideoCuts(videoCuts);
                videoCutsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空disposable防止内存泄漏
        mDisposable.dispose();
    }

    //编写按钮逻辑
    //能否把mDisposable的相关操作移动到数据库中？

    public void ClearVideoCuts() {
        mDisposable.add(videoCutsViewModel.ClearVideoCuts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Toast.makeText(this, "清空列表成功", Toast.LENGTH_SHORT).show(),
                        throwable -> {
                            Toast.makeText(this, "清空列表失败", Toast.LENGTH_SHORT).show();
                        })
        );
    }

    public void UpdateVideoCut(VideoCut... videoCuts) {
        mDisposable.add(videoCutsViewModel.UpdateVideoCuts(videoCuts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    public void InsertVideoCuts(List<VideoCut> videoCutList) {
        mDisposable.add(videoCutsViewModel.InsertVideoCuts(videoCutList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list1 -> Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show(), throwable -> {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                })
        );
    }

    public void DeleteVideoCuts(VideoCut... videoCuts) {
        mDisposable.add(videoCutsViewModel.DeleteVideoCuts(videoCuts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    private void StartGetVideoCutActivity() {
        startActivityForResult(new Intent(this, GetVideoCutActivity.class), 1);
    }
    private void StartEditActivity(){
        startActivityForResult(new Intent(this, EditActivity.class), 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        List<VideoCut> list = data.getBundleExtra("videoCuts").getParcelableArrayList("videoCuts");
                        InsertVideoCuts(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    try {
                        String newName,newDes;
                        newName = data.getStringExtra("newName");
                        newDes = data.getStringExtra("newDes");
                        if(newName != null){
                            videoCutEditor.setName(newName);
                        }
                        if(newDes != null){
                            videoCutEditor.setDescription(newDes);
                        }
                        UpdateVideoCut(videoCutEditor);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}