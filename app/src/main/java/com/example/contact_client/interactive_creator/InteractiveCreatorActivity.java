package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
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
import com.example.contact_client.repository.VideoProject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;

public class InteractiveCreatorActivity extends AppCompatActivity {
    //用于对数据库的异步操作
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //当前activity的binding
    private ActivityInteracitveCreatorBinding mBinding;
    //绑定ViewModel
    private CreatorViewModel mViewModel;
    //适配器
    private SonVideoCutsAdapter sonVideoCutsAdapter;
    //用于指向需要编辑的VideoNode
    private VideoNode nodeEditor;
    //定义常用量
    private static final int ROOT_NODE = -1;
    private static final int ADD_VIDEO = 1;
    private static final int ADD_NODE = 2;
    private static final int CHANGE_VIDEO = 3;
    private static final int CHANGE_NODE = 4;
    private static final int JUMP = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定binding
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_interacitve_creator);
        mBinding.setLifecycleOwner(this);
        //绑定viewModel
        mViewModel = new ViewModelProvider(this).get(CreatorViewModel.class);
        //初始化
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    void init(){
        //绑定数据
        bindDataToViewModel(getIntent().getParcelableExtra(getString(R.string.videoProject)));
        //配置recyclerView
        modifyRecyclerView();
        //注册按键功能
        registerButtonEvents();
        //初始化
        initUI();
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
            case ADD_VIDEO:
                if(resultCode==RESULT_OK){
                    try {
                        //添加从数据库中获取的data
                        List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                        //添加数据
                        sonVideoCutsAdapter.insertData(list);
                        Toast.makeText(this,"添加了"+list.size()+"个视频",Toast.LENGTH_SHORT).show();
                        saveVideoCutsToCurrentNode();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case ADD_NODE:
                if(resultCode==RESULT_OK){
                    try {
                        //直接添加儿子节点
                        List<Integer> newList = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                        mViewModel.addSonNodes(newList);
                        rebuildSonList(mViewModel.getVideoNode());
                        Toasty.success(this,"添加结点成功",Toast.LENGTH_SHORT,true).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toasty.error(this,"添加结点失败",Toast.LENGTH_SHORT,true).show();
                    }
                }
                break;
            case CHANGE_VIDEO:
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
            case CHANGE_NODE:
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
            case JUMP:
                if(resultCode==RESULT_OK){
                    //跳转
                    try{
                            List<Integer> list = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                            if(list.get(0)!=null){
                                jumpToNode(list.get(0));
                            }
                        } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void jumpToNode(int index) {
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
        mViewModel.setCurrentNode(index);
        rebuildSonList(newNode);
        Toasty.info(this, "跳转到结点P"+index, Toast.LENGTH_SHORT,true).show();
    }

    public void goBack(){
        int fatherIndex = mViewModel.getVideoNode().getLastNodeIndex();
        if(fatherIndex==-1){
            Toasty.error(this,"没有上一层!",Toast.LENGTH_SHORT).show();
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
            mViewModel.setCurrentNode(fatherIndex);
            rebuildSonList(fatherNode);
        }
    }

    void deleteNode(@NotNull VideoNode videoNode, @NotNull VideoNode fatherNode){
        mViewModel.deleteNode(videoNode.getIndex(),fatherNode.getIndex());
    }

    public void updateFatherNodeUI(@NotNull VideoCut videoCut, int newIndex){
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

    public void rebuildSonList(@NotNull VideoNode fatherNode){
        sonVideoCutsAdapter.setCurrentNode(fatherNode);
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
                        if(ids.get(i)== ROOT_NODE){
                            mViewModel.getSonVideoCuts().add(mViewModel.getRootVideoCut());
                        }else{
                            for(int j=0;j<videoCuts.size();j++){
                                if(videoCuts.get(j).getId()==ids.get(i)){
                                    mViewModel.getSonVideoCuts().add(videoCuts.get(j));
                                    break;
                                }
                            }
                        }
                    }
                    sonVideoCutsAdapter.setAllVideoCuts(mViewModel.getSonVideoCuts());
                    Log.d("mylo","update VideoCuts: "+mViewModel.getSonVideoCuts().toString());
                }, throwable -> Log.d("mylo", "accept: Unable to get sons by id!")));
    }

    public void saveVideoCutsToCurrentNode(){
        boolean b = mViewModel.saveVideoCutsToCurrentNode();
        if(!b) Toasty.error(this,"保存结点信息失败",Toasty.LENGTH_SHORT).show();
    }

    void searchRoom(int requestCode){
        startActivityForResult(new Intent(this, SearchRoomForVideoCutActivity.class),requestCode);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    void searchVideoNodeForIndexes(int requestCode){
        Intent intent = new Intent(this, SearchVideoNodeActivity.class);
        List<VideoNode> notIsolatedNodes = new ArrayList<>();
        List<VideoNode> allNodes = mViewModel.getVideoProject().getVideoNodeList();
        for(int i=0;i<allNodes.size();i++){
            if(allNodes.get(i).getId()!=VideoProject.ISOLATED){
                notIsolatedNodes.add(allNodes.get(i));
            }
        }
        intent.putParcelableArrayListExtra(getString(R.string.videoNodes), (ArrayList<? extends Parcelable>) notIsolatedNodes);
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    void saveProjectToDataBase(){
        mDisposable.add(mViewModel.insertVideoProject(mViewModel.getVideoProject())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {mViewModel.getVideoProject().setId(aLong); Toasty.success(this,"保存成功",Toast.LENGTH_SHORT,true).show();}));
    }
    void showPopupMenu(View view,int requestCodeVideoCut,int requestCodeVideoNode){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_creator,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.newOne){
                if(requestCodeVideoCut== CHANGE_VIDEO &&nodeEditor.getIndex()==0){
                    Toasty.info(this,"无法改变根结点视频",Toast.LENGTH_SHORT).show();
                }else{
                    searchRoom(requestCodeVideoCut);
                }
            }else if(item.getItemId()==R.id.fromOld){
                searchVideoNodeForIndexes(requestCodeVideoNode);
            }
            return false;
        });
        popupMenu.show();
    }

    void bindDataToViewModel(VideoProject videoProject){
        if(mViewModel==null){
            Toasty.error(this,"绑定数据失败",Toast.LENGTH_SHORT).show();
            return;
        }
        if(videoProject==null){
            videoProject = new VideoProject("VideoProject","I am a Project!");
            VideoNode videoNode = new VideoNode(-1,0,-1);
            videoProject.addNode(videoNode);
        }
        mViewModel.setVideoProject(videoProject);
        mViewModel.setCurrentNode(0);
    }

    void modifyRecyclerView(){
        modifyRecyclerViewAdapter();
        modifyRecyclerViewOnClick();
    }

    void modifyRecyclerViewAdapter(){
        sonVideoCutsAdapter = new SonVideoCutsAdapter(mViewModel.getSonVideoCuts(),mViewModel.getVideoProject().getVideoNodeList(),mViewModel.getVideoNode());
        SlideInLeftAnimationAdapter slideInLeftAnimationAdapter = new SlideInLeftAnimationAdapter(sonVideoCutsAdapter);
        slideInLeftAnimationAdapter.setDuration(1000);
        slideInLeftAnimationAdapter.setInterpolator(new OvershootInterpolator());
        slideInLeftAnimationAdapter.setFirstOnly(false);
        mBinding.recyclerView.setAdapter(slideInLeftAnimationAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void modifyRecyclerViewOnClick(){
        mBinding.recyclerView.post(() -> sonVideoCutsAdapter.setOnClickItem(new SonVideoCutsAdapter.onClickItem() {
            @Override
            public void onClickChangeNode(View v, int position) {
                nodeEditor = mViewModel.getVideoProject().getVideoNodeList().get(mViewModel.getVideoNode().getSons().get(position));
                showPopupMenu(v, CHANGE_VIDEO, CHANGE_NODE);
            }
            @Override
            public void onClickDeleteNode(View v, int position) {
                sonVideoCutsAdapter.removeData(position);
                VideoNode targetNode = mViewModel.getVideoProject().getVideoNodeList().get(mViewModel.getVideoNode().getSons().get(position));
                VideoNode fatherNode = mViewModel.getVideoNode();
                //移除此子节点
                fatherNode.getSons().remove((Integer)targetNode.getIndex());
                //如果子节点已经没有任何父亲则移动到孤立
                Log.d("mylo",targetNode.getIndex() +" "+fatherNode.getIndex());
                deleteNode(targetNode,fatherNode);
            }
            @Override
            public void onClick(View v, int position) {
                //保存子节点
                saveVideoCutsToCurrentNode();
                //设置新父亲
                mViewModel.setCurrentNode(mViewModel.getVideoNode().getSons().get(position));
                //更新父亲节点
                VideoCut videoCut = sonVideoCutsAdapter.getAllVideoCuts().get(position);
                if(videoCut.getId()==-1){
                    updateRootNodeUI();
                }else{
                    updateFatherNodeUI(videoCut,mViewModel.getVideoNode().getIndex());
                }
                //更新列表并通知Adapter
                rebuildSonList(mViewModel.getVideoNode());
            }
        }));
    }

    void registerButtonEvents(){
        mBinding.floatingMenuItemAddVideo.setOnClickListener(v -> searchRoom(ADD_VIDEO));
        mBinding.floatingMenuItemAddNode.setOnClickListener(v -> searchVideoNodeForIndexes(ADD_NODE));
        mBinding.buttonBack.setOnClickListener(v -> {
            saveVideoCutsToCurrentNode();
            goBack();
        });
        mBinding.floatingMenuItemSave.setOnClickListener(v-> saveProjectToDataBase());
        mBinding.buttonJumpTo.setOnClickListener(v->searchVideoNodeForIndexes(JUMP));
    }

    void initUI(){
        if(mViewModel.getVideoNode()==null){
            jumpToNode(0);
        }else{
            jumpToNode(mViewModel.getVideoNode().getIndex());
        }
    }
}