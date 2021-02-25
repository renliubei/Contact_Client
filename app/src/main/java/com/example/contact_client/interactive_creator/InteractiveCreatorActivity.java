package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InteractiveCreatorActivity extends AppCompatActivity {
    //用于对数据库的异步操作
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //当前activity的binding
    private ActivityInteracitveCreatorBinding mBinding;
    //绑定ViewModel
    private CreatorViewModel mViewModel;
    //适配器
    private SonVideoCutsAdapter sonVideoCutsAdapter;

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
        mBinding.buttonAdd.setOnClickListener(this::showPopupMenu);
        mBinding.buttonBack.setOnClickListener(v->goBack());
        mBinding.buttonSave.setOnClickListener(v->saveToDataBase());
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                sonVideoCutsAdapter.setOnClickItem(new SonVideoCutsAdapter.onClickItem() {
                    @Override
                    public void onClickDelete(View v, int position) {
                        //需要写对数据的删除
                        sonVideoCutsAdapter.removeData(position);
                        //没有保存的情况下删除会出错！
                        try {
                            deleteNode(mViewModel.getVideoProject().getVideoNodeList().get(mViewModel.getVideoNode().getSons().get(position)));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onClick(View v, int position) {
                        //保存子节点并设置新节点
                        saveNodeList(position);
                        //更新父亲节点
                        updateFatherVideoCut(sonVideoCutsAdapter.getAllVideoCuts().get(position),mViewModel.getVideoNode().getIndex());
                        //更新列表并通知Adapter
                        rebuildSonList(mViewModel.getVideoNode());
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
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
                        //添加数据
                        sonVideoCutsAdapter.insertData(list);
                        saveNodeList(-1);
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    try {
                        List<Integer> oldlist = mViewModel.getVideoNode().getSons();
                        List<Integer> newlist = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                        for(int i=0;i<newlist.size();i++){
                            if(!oldlist.contains(newlist.get(i))){
                                oldlist.add(newlist.get(i));
                            }
                        }
                        rebuildSonList(mViewModel.getVideoNode());
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    public void goBack(){
        saveNodeList(-1);
        int fatherIndex = mViewModel.getVideoNode().getFatherVideoCutIndex();
        if(fatherIndex==-1){
            Toast.makeText(this,"没有上一层!",Toast.LENGTH_SHORT).show();
        }else{
            VideoNode fatherNode = mViewModel.getVideoProject().getVideoNodeList().get(fatherIndex);
            Log.d("mylo", fatherNode.getId() +" "+ fatherIndex);
            if(fatherIndex==0){
                //返回根节点
                returnToRoot();
            }else{
                //返回父节点
                mDisposable.add(mViewModel.getById(fatherNode.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<VideoCut>() {
                            @Override
                            public void accept(VideoCut videoCut) throws Exception {
                                updateFatherVideoCut(videoCut,fatherNode.getIndex());
                            }
                        }, Throwable::printStackTrace));
            }
            mViewModel.setVideoNode(fatherNode);
            rebuildSonList(fatherNode);
        }

    }

    public void deleteNode(VideoNode videoNode){
        //父亲删除
        VideoNode father = mViewModel.getVideoProject().getVideoNodeList().get(videoNode.getFatherVideoCutIndex());
        //在sons中删除
        for(int i=0;i<father.getSons().size();i++){
            if(father.getSons().get(i)==videoNode.getIndex()){
                father.getSons().remove(i);
                break;
            }
        }
        //儿子删除
        videoNode.setDeleted(true);
    }

    public void updateFatherVideoCut(VideoCut videoCut,int newIndex){
        mBinding.fatherName.setText(videoCut.getName());
        mBinding.fatherDescription.setText(videoCut.getDescription());
        mBinding.textViewIndex.setText("P"+newIndex);
        Glide.with(this)
                .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(mBinding.fatherIcon);
    }


    public void returnToRoot(){
        mBinding.fatherName.setText(R.string.startVideos);
        mBinding.fatherDescription.setText(R.string.startHint);
        mBinding.textViewIndex.setText(R.string.Root);
        Glide.with(this)
                .load(R.drawable.ic_baseline_home_48)
                .into(mBinding.fatherIcon);
    }

    public void rebuildSonList(VideoNode fatherNode){
        List<Long> ids = new ArrayList<>();
        for(int i=0;i<fatherNode.getSons().size();i++){
            ids.add(mViewModel.getVideoProject().getVideoNodeList().get(fatherNode.getSons().get(i)).getId());
        }
        Log.d("mylo","sons id are : "+ids.toString());
        mDisposable.add(mViewModel.getAllById(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoCuts -> {
                    mViewModel.getSonVideoCuts().clear();
                    //存在较大的性能隐患，可以考虑添加进度条显示
                    for(int i=0;i<ids.size();i++){
                        for(int j=0;j<videoCuts.size();j++){
                            if(videoCuts.get(j).getId()==ids.get(i)){
                                mViewModel.getSonVideoCuts().add(videoCuts.get(j));
                                break;
                            }
                        }
                    }
                    sonVideoCutsAdapter.setAllVideoCuts(mViewModel.getSonVideoCuts());
                    Log.d("mylo","update VideoCuts: "+mViewModel.getSonVideoCuts().toString());
                }, throwable -> Log.d("mylo", "accept: Unable to get sons by id!")));
    }

    public void saveNodeList(int newNodePosition){
        //储存节点！
        //策略:相同位置替换Id，超过原有sonList长度则添加
        //删除需要另写函数
        try {
            VideoNode videoNode;
            VideoNode fatherNode = mViewModel.getVideoNode();
            for(int i=0;i<mViewModel.getSonVideoCuts().size();i++){
                //替换Id
                if(i<fatherNode.getSons().size()){
                    //取出已存的sonNode
                    videoNode = mViewModel.getVideoProject().getVideoNodeList().get(fatherNode.getSons().get(i));
                    //将Id替换为点击的VideoCut的Id
                    videoNode.setId(mViewModel.getSonVideoCuts().get(i).getId());
                    Toast.makeText(this,"覆盖",Toast.LENGTH_SHORT).show();
                }else{
                    //新增
                    VideoCut videoCut = mViewModel.getSonVideoCuts().get(i);
                    videoNode = new VideoNode(fatherNode.getIndex(),mViewModel.getVideoProject().getListSize(),videoCut.getId(),videoCut.getName());
                    fatherNode.addSons(mViewModel.getVideoProject().getListSize());
                    mViewModel.getVideoProject().addNode(videoNode);
                }
                if(newNodePosition!=-1&&i==newNodePosition){
                    mViewModel.setVideoNode(videoNode);
                }
            }
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            Log.d("mylo","videoNodes are: "+mViewModel.getVideoProject().getVideoNodeList().toString());
            Log.d("mylo","sons are: "+fatherNode.getSons().toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_SHORT).show();
        }
    }

    void searchRoomForAdd(){
        startActivityForResult(new Intent(this, SearchRoomForVideoCutActivity.class),1);
    }

    void searchVideoProjectForAdd(){
        Intent intent = new Intent(this, SearchVideoNodeActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.videoNodes), (ArrayList<? extends Parcelable>) mViewModel.getVideoProject().getVideoNodeList());
        startActivityForResult(intent,2);
    }

    void saveToDataBase(){
        mDisposable.add(mViewModel.insertVideoProject(mViewModel.getVideoProject())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_creator,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.newOne){
                searchRoomForAdd();
            }else if(item.getItemId()==R.id.fromOld){
                searchVideoProjectForAdd();
            }
            return false;
        });
        popupMenu.show();
    }
}