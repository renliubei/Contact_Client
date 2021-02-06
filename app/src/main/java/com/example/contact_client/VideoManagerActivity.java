package com.example.contact_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoManagerBinding;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoManagerActivity extends AppCompatActivity {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private ActivityVideoManagerBinding binding;
    private VideoCutDatabase videoCutDatabase;

    private VideoCutDao videoCutDao;

    private LiveData<List<VideoCut>> allLiveDataVideoCuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取数据库和binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_manager);
        videoCutDatabase = VideoCutDatabase.getVideoCutDatabase(this);
        videoCutDao = videoCutDatabase.getVideoCutDao();
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
        allLiveDataVideoCuts = videoCutDao.getAllVideoCuts();
        allLiveDataVideoCuts.observe(this, new Observer<List<VideoCut>>() {
            @Override
            public void onChanged(List<VideoCut> videoCuts) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < videoCuts.size(); i++) {
                    VideoCut vv = videoCuts.get(i);
                    stringBuilder.append(vv.getId()).append(vv.getName()).append(" ").append(vv.getDescription()).append(" ").append(vv.getLenth()).append("\n");
                }
                binding.textViewData.setText(stringBuilder);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void InsertVideoCuts() {
        VideoCut videoCut1 = new VideoCut("hello", "nonsense", 3);
        VideoCut videoCut2 = new VideoCut("hello", "also nonsense", 4);
        mDisposable.add(videoCutDao.insertVideoCuts(videoCut1, videoCut2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        Toast.makeText(this, "插入", Toast.LENGTH_SHORT).show();
    }

    private void ClearVideoCuts() {
        mDisposable.add(videoCutDao.deleteAllVideoCuts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        Toast.makeText(this, "清空", Toast.LENGTH_SHORT).show();
    }

    private void DeleteVideoCuts() {
        VideoCut videoCut = new VideoCut("", "", 1);
        videoCut.setId(5);
        mDisposable.add(videoCutDao.deleteVideoCuts(videoCut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
    }

    private void UpdateVideoCuts() {
        VideoCut videoCut = new VideoCut("updater", "I'm updated", 1);
        videoCut.setId(3);
        mDisposable.add(videoCutDao.deleteVideoCuts(videoCut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        Toast.makeText(this, "更新", Toast.LENGTH_SHORT).show();
    }
}