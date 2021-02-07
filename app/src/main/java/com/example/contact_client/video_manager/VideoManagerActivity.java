package com.example.contact_client.video_manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private VideoCutsViewModel videoCutsViewModel;

    private VideoCutsAdapter videoCutsAdapter;

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

        //为button添加事件
        binding.buttonInsert.setOnClickListener(v -> InsertVideoCuts());
        binding.buttonClear.setOnClickListener(v -> ClearVideoCuts());
        binding.buttonUpdate.setOnClickListener(v -> UpdateVideoCuts());
        binding.buttonDelete.setOnClickListener(v -> DeleteVideoCuts());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //为Livedata添加observer
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
        //清空disposable防止内存泄漏
        mDisposable.clear();
    }

    //编写按钮逻辑
    //是否能把mDisposable的相关操作移动到数据库中？
    private void DeleteVideoCuts() {
//        VideoCut videoCut1 = new VideoCut("", "", 1);
//        videoCut1.setId(5);
//        mDisposable.add(videoCutsViewModel.DeleteVideoCuts(videoCut1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//        );
    }

    private void UpdateVideoCuts() {
//        VideoCut videoCut1 = new VideoCut("hhhhhhh", "I am updated！", 1);
//        videoCut1.setId(15);
//        mDisposable.add(videoCutsViewModel.UpdateVideoCuts(videoCut1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//        );
    }

    private void ClearVideoCuts() {
//        mDisposable.add(videoCutsViewModel.ClearVideoCuts()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//        );
    }

    private void InsertVideoCuts() {
        Intent intent = new Intent(this,GetVideoCutActivity.class);
        startActivity(intent);
//        VideoCut videoCut1 = new VideoCut("ydm", "god", 3);
//        VideoCut videoCut2 = new VideoCut("mdy", "dog", 2);
//        mDisposable.add(videoCutsViewModel.InsertVideoCuts(videoCut1, videoCut2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//        );
    }

}