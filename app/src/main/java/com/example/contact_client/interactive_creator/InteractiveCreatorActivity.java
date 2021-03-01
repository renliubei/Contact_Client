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
    //
    private VideoNode nodeEditor;

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
        mBinding.buttonAdd.setOnClickListener(v->showPopupMenu(v,1,2));
        mBinding.buttonBack.setOnClickListener(v -> {
            saveVideoCutsToNodes(-1);
            goBack();
        });
        mBinding.buttonSave.setOnClickListener(v-> saveProjectToDataBase());
        mBinding.buttonJumpTo.setOnClickListener(v->searchVideoNodeForIndexes(5));
        mBinding.recyclerView.post(() -> sonVideoCutsAdapter.setOnClickItem(new SonVideoCutsAdapter.onClickItem() {
            @Override
            public void onClickEdit(View v, int position) {
                nodeEditor = mViewModel.getVideoProject().getVideoNodeList().get(mViewModel.getVideoNode().getSons().get(position));
                showPopupMenu(v,3,4);
            }
            @Override
            public void onClickDelete(View v, int position) {
                //需要写对数据的删除
                sonVideoCutsAdapter.removeData(position);
                //没有保存的情况下删除会出错！
                try {
                    VideoNode targetNode = mViewModel.getVideoProject().getVideoNodeList().get(mViewModel.getVideoNode().getSons().get(position));
                    VideoNode fatherNode = mViewModel.getVideoNode();
                    //移除此子节点
                    fatherNode.getSons().remove((Integer)targetNode.getIndex());
                    //如果子节点已经没有任何父亲则移动到孤立
                    deleteFatherAndCheckIsolation(targetNode,fatherNode);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onClick(View v, int position) {
                //保存子节点并设置新节点
                saveVideoCutsToNodes(position);
                //更新父亲节点
                updateFatherNodeUI(sonVideoCutsAdapter.getAllVideoCuts().get(position),mViewModel.getVideoNode().getIndex());
                //更新列表并通知Adapter
                rebuildSonList(mViewModel.getVideoNode());
            }
        }));
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
            /*
                1:从数据库中添加视频
                2:从结点列表中添加结点
                3:修改儿子节点的视频Id
                4:修改儿子结点
                5:跳转到某个结点
             */
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        //添加从数据库中获取的data
                        List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                        //添加数据
                        sonVideoCutsAdapter.insertData(list);
                        saveVideoCutsToNodes(-1);
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
                        //直接添加儿子节点
                        List<Integer> oldList = mViewModel.getVideoNode().getSons();
                        List<Integer> newList = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                        for(int i=0;i<newList.size();i++){
                            if(!oldList.contains(newList.get(i))){
                                oldList.add(newList.get(i));
                            }
                        }
                        rebuildSonList(mViewModel.getVideoNode());
                        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 3:
                if(resultCode==RESULT_OK){
                    try{
                        //修改儿子视频
                        List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                        if(list.get(0)!=null){
                            nodeEditor.setId(list.get(0).getId());
                            rebuildSonList(mViewModel.getVideoNode());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case 4:
                if(resultCode==RESULT_OK){
                    try{
                        //修改儿子节点
                        List<Integer> list = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                        if(list.get(0)!=null){
                            List<Integer> sons = mViewModel.getVideoNode().getSons();
                            //改儿子
                            sons.set(sons.indexOf(nodeEditor.getIndex()),list.get(0));
                            rebuildSonList(mViewModel.getVideoNode());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case 5:
                if(resultCode==RESULT_OK){
                    try{
                            List<Integer> list = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                            if(list.get(0)!=null){
                                Jump(list.get(0));
                            }
                        } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void Jump(int index) {
        VideoNode newNode = mViewModel.getVideoProject().getVideoNodeList().get(index);
        newNode.setLastNodeIndex(mViewModel.getVideoNode().getIndex());
        if (index == 0) {
            updateRootNodeUI();
        } else {
            mDisposable.add(mViewModel.getVideoCutById(newNode.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(videoCut -> updateFatherNodeUI(videoCut, newNode.getIndex())));
        }
        mViewModel.setVideoNode(newNode);
        rebuildSonList(newNode);
        Toast.makeText(this, "跳转成功", Toast.LENGTH_SHORT).show();
    }

    public void goBack(){
        int fatherIndex = mViewModel.getVideoNode().getLastNodeIndex();
        if(fatherIndex==-1){
            Toast.makeText(this,"没有上一层!",Toast.LENGTH_SHORT).show();
        }else{
            VideoNode fatherNode = mViewModel.getVideoProject().getVideoNodeList().get(fatherIndex);
            Log.d("mylo", fatherNode.getId() +" "+ fatherIndex);
            if(fatherIndex==0){
                //返回根节点
                updateRootNodeUI();
            }else{
                //返回父节点
                mDisposable.add(mViewModel.getVideoCutById(fatherNode.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(videoCut -> updateFatherNodeUI(videoCut,fatherNode.getIndex()), Throwable::printStackTrace));
            }
            mViewModel.setVideoNode(fatherNode);
            rebuildSonList(fatherNode);
        }
    }

    void deleteFatherAndCheckIsolation(VideoNode videoNode, VideoNode fatherNode){
        //删除父亲
        videoNode.getFathers().remove((Integer)fatherNode.getIndex());
        //如果此结点已经没有任何父亲
        if(videoNode.getFathers().size()==0){
            //移动到被删除列表
            mViewModel.getVideoProject().getIsolatedNodes().add(videoNode);
            Log.d("mylo","P" + videoNode.getIndex() + " deleted: " + mViewModel.getVideoProject().getIsolatedNodes().toString());
            //递归判断其儿子是否也被孤立
            for(int i=0;i<videoNode.getSons().size();i++){
                deleteFatherAndCheckIsolation(mViewModel.getVideoProject().getVideoNodeList().get(videoNode.getSons().get(i)),videoNode);
            }
            videoNode.getSons().clear();
        }
    }

    public void updateFatherNodeUI(VideoCut videoCut, int newIndex){
        mBinding.fatherName.setText(videoCut.getName());
        mBinding.fatherDescription.setText(videoCut.getDescription());
        mBinding.textViewIndex.setText(getString(R.string.Index,newIndex));
        Glide.with(this)
                .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(mBinding.fatherIcon);
    }


    public void updateRootNodeUI(){
        mBinding.fatherName.setText(R.string.startVideos);
        mBinding.fatherDescription.setText(R.string.startHint);
        mBinding.textViewIndex.setText(R.string.Root);
        Glide.with(this)
                .load(R.drawable.ic_baseline_home_48)
                .into(mBinding.fatherIcon);
    }

    public void rebuildSonList(VideoNode fatherNode){
        List<Long> ids = new ArrayList<>();
        List<Integer> sons = fatherNode.getSons();
        List<VideoNode> list = mViewModel.getVideoProject().getVideoNodeList();
        for(int i=0;i<fatherNode.getSons().size();i++){
            ids.add(list.get(sons.get(i)).getId());
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

    public void saveVideoCutsToNodes(int newNodePosition){
        //储存节点！
        //策略:相同位置替换Id，超过原有sonList长度则添加
        //删除需要另写函数
        try {
            VideoNode videoNode;
            VideoCut videoCut;
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
                    videoCut = mViewModel.getSonVideoCuts().get(i);
                    //如果有被删除结点，回收
                    if(mViewModel.getVideoProject().getIsolatedNodes().size()>0){
                        videoNode = mViewModel.getVideoProject().getIsolatedNodes().remove(0);
                        videoNode.setId(videoCut.getId());
                        videoNode.setName(videoCut.getName());
                        videoNode.setLastNodeIndex(fatherNode.getIndex());
                    }else{
                    //不存在删除结点，新增
                        videoNode = new VideoNode(fatherNode.getIndex(),mViewModel.getVideoProject().getListSize(),videoCut.getId(),videoCut.getName());
                        mViewModel.getVideoProject().addNode(videoNode);
                    }
                    videoNode.addFather(fatherNode.getIndex());
                    fatherNode.addSon(videoNode.getIndex());
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

    void searchRoom(int requestCode){
        startActivityForResult(new Intent(this, SearchRoomForVideoCutActivity.class),requestCode);
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
    }

    void searchVideoNodeForIndexes(int requestCode){
        Intent intent = new Intent(this, SearchVideoNodeActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.videoNodes), (ArrayList<? extends Parcelable>) mViewModel.getVideoProject().getVideoNodeList());
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
    }

    void saveProjectToDataBase(){
        mDisposable.add(mViewModel.insertVideoProject(mViewModel.getVideoProject())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mViewModel.getVideoProject().setId(aLong)));
    }

    void showPopupMenu(View view,int requestCodeNewOne,int requestCodeFromOld){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_creator,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.newOne){
                searchRoom(requestCodeNewOne);
            }else if(item.getItemId()==R.id.fromOld){
                searchVideoNodeForIndexes(requestCodeFromOld);
            }
            return false;
        });
        popupMenu.show();
    }
}