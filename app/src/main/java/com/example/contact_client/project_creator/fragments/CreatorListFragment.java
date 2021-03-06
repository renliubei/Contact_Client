package com.example.contact_client.project_creator.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityInteracitveCreatorBinding;
import com.example.contact_client.project_creator.CreatorViewModel;
import com.example.contact_client.project_creator.SearchRoomForVideoCutActivity;
import com.example.contact_client.project_creator.SearchVideoNodeActivity;
import com.example.contact_client.project_creator.VideoNode;
import com.example.contact_client.project_creator.adapters.SonVideoCutsAdapter;
import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoProject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatorListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatorListFragment extends Fragment {
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

    public CreatorListFragment() {
        // Required empty public constructor
    }

    public static CreatorListFragment newInstance(CreatorViewModel viewModel) {
        CreatorListFragment fragment = new CreatorListFragment();
        fragment.mViewModel = viewModel;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.activity_interacitve_creator,container,false);
        mBinding.setLifecycleOwner(getActivity());
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getVideoNodeMutableLiveData().observe(this, new Observer<VideoNode>() {
            @Override
            public void onChanged(VideoNode node) {
                updateCardUI(node);
                rebuildSonList(node);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }


    void init(){
        //配置recyclerView
        modifyRecyclerView();
        //注册按键功能
        registerButtonEvents();
        //初始化
        initUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*
                1:从数据库中添加视频
                2:从结点列表中添加结点
                3:修改儿子节点的视频Id
                4:修改儿子结点
                5:跳转到某个结点
             */
            case ADD_VIDEO:
                if (resultCode == RESULT_OK) {
                    //添加从数据库中获取的data
                    List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                    //添加数据
                    if(list!=null&&list.size()>0){
                        sonVideoCutsAdapter.insertData(list);
                        Toast.makeText(getContext(), "添加了" + list.size() + "个视频", Toast.LENGTH_SHORT).show();
                        saveVideoCutsToCurrentNode();
                    }
                }
                break;
            case ADD_NODE:
                if (resultCode == RESULT_OK) {
                    //直接添加儿子节点
                    List<Integer> newList = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                    if(newList!=null&&newList.size()>0){
                        mViewModel.addSonNodes(newList);
                        rebuildSonList(mViewModel.getVideoNode());
                        Toast.makeText(getContext(), "添加了"+newList.size()+"个结点", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case CHANGE_VIDEO:
                if (resultCode == RESULT_OK) {
                    List<VideoCut> list = data.getParcelableArrayListExtra("videoCuts");
                    if (list!=null&&list.get(0) != null) {
                        nodeEditor.setId(list.get(0).getId());
                        rebuildSonList(mViewModel.getVideoNode());
                    }
                }
                break;
            case CHANGE_NODE:
                if (resultCode == RESULT_OK) {
                    //修改儿子节点
                    List<Integer> list = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                    if (list!=null&&list.get(0) != null) {
                        List<Integer> sons = mViewModel.getVideoNode().getSons();
                        //改儿子
                        sons.set(sons.indexOf(nodeEditor.getIndex()), list.get(0));
                        rebuildSonList(mViewModel.getVideoNode());
                    }
                }
                break;
            case JUMP:
                if (resultCode == RESULT_OK) {
                    List<Integer> list = data.getIntegerArrayListExtra(getString(R.string.videoNodeIndexes));
                    if (list != null && list.size() > 0)
                        jumpToNode(list.get(0));
                }
        }
    }

    public void jumpToNode(int index) {
        mViewModel.jumpToNode(index);
    }

    public void goBack(){
        int fatherIndex = mViewModel.getVideoNode().getLastNodeIndex();
        if(fatherIndex==-1){
            Toasty.error(getContext(),"没有上一层!",Toast.LENGTH_SHORT).show();
        }else{
            mViewModel.setCurrentNode(fatherIndex);
        }
    }

    void deleteNode(@NotNull VideoNode videoNode, @NotNull VideoNode fatherNode){
        mViewModel.deleteNode(videoNode.getIndex(),fatherNode.getIndex());
    }
    void deleteNode(int nodeIndex,int fatherIndex){
        mViewModel.deleteNode(nodeIndex,fatherIndex);
    }
    /**
     * 根据该结点更新上方铭牌
     * @param node 结点
     */
    public void updateCardUI(VideoNode node){
        if(node.getIndex()==0) updateCardUIToRoot();
        else{
            mDisposable.add(mViewModel.getVideoCutById(node.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(videoCut -> updateCardUIByCut(videoCut,node.getIndex()), Throwable::printStackTrace));
        }
    }

    /**
     * 通过给定的cut来更新铭牌
     * @param videoCut 提供信息的cut
     * @param newIndex 展示在铭牌上的结点编号
     */
    public void updateCardUIByCut(@NotNull VideoCut videoCut, int newIndex){
        mBinding.fatherName.setText(videoCut.getName());
        mBinding.fatherDescription.setText(videoCut.getDescription());
        mBinding.textViewIndex.setText(getString(R.string.Index,newIndex));
        Glide.with(this)
                .load(Uri.fromFile(new File(videoCut.getThumbnailPath())))
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(mBinding.fatherIcon);
    }

    /**
     * 将铭牌更新为根节点
     */
    public void updateCardUIToRoot(){
        mBinding.fatherName.setText(R.string.startVideos);
        mBinding.fatherDescription.setText(R.string.startHint);
        mBinding.textViewIndex.setText(R.string.Root);
        Glide.with(this)
                .load(R.drawable.ic_baseline_home_48)
                .into(mBinding.fatherIcon);
    }

    /**
     * 根据提供的结点更新列表
     * @param node 结点信息
     */
    public void rebuildSonList(@NotNull VideoNode node){
        sonVideoCutsAdapter.setCurrentNode(node);
        List<Long> ids = new ArrayList<>();
        List<Integer> sons = node.getSons();
        List<VideoNode> list = mViewModel.getVideoProject().getVideoNodeList();
        for(int index:sons){
            ids.add(list.get(index).getId());
        }
        mDisposable.add(mViewModel.getAllById(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoCuts -> {
                    sonVideoCutsAdapter.getAllVideoCuts().clear();
                    sonVideoCutsAdapter.notifyDataSetChanged();
                    //存在较大的性能隐患，可以考虑添加进度条显示
                    HashMap<Long,VideoCut> map = new HashMap<>();
                    for(VideoCut cut:videoCuts)
                        map.put(cut.getId(),cut);
                    for(Long id:ids){
                        if(id==ROOT_NODE){
                            sonVideoCutsAdapter.insertData(mViewModel.getRootVideoCut());
                        }else{
                            VideoCut cut = map.get(id);
                            if(cut!=null)
                                sonVideoCutsAdapter.insertData(cut);
                        }
                    }
                }, throwable -> Log.d("mylo", "accept: Unable to get sons by id!")));
    }

    public void saveVideoCutsToCurrentNode(){
        boolean b = mViewModel.saveVideoCutsToCurrentNode();
        if(!b) Toasty.error(getContext(),"保存结点信息失败",Toasty.LENGTH_SHORT).show();
    }

    void searchRoom(int requestCode){
        startActivityForResult(new Intent(getContext(), SearchRoomForVideoCutActivity.class),requestCode);
    }

    void searchVideoNodeForIndexes(int requestCode){
        Intent intent = new Intent(getContext(), SearchVideoNodeActivity.class);
        List<VideoNode> notIsolatedNodes = new ArrayList<>();
        List<VideoNode> allNodes = mViewModel.getVideoProject().getVideoNodeList();
        for(int i=0;i<allNodes.size();i++){
            if(allNodes.get(i).getId()!= VideoProject.ISOLATED){
                notIsolatedNodes.add(allNodes.get(i));
            }
        }
        intent.putParcelableArrayListExtra(getString(R.string.videoNodes), (ArrayList<? extends Parcelable>) notIsolatedNodes);
        startActivityForResult(intent,requestCode);

    }

    void saveProjectToDataBase(){
        mDisposable.add(mViewModel.insertVideoProject(mViewModel.getVideoProject())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {mViewModel.getVideoProject().setId(aLong); Toasty.success(getContext(),"保存成功",Toast.LENGTH_SHORT,true).show();}));
    }
    void showPopupMenu(View view,int requestCodeVideoCut,int requestCodeVideoNode){
        PopupMenu popupMenu = new PopupMenu(getContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_creator,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.newOne){
                if(requestCodeVideoCut== CHANGE_VIDEO &&nodeEditor.getIndex()==0){
                    Toasty.info(getContext(),"无法改变根结点视频",Toast.LENGTH_SHORT).show();
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

    void modifyRecyclerView(){
        modifyRecyclerViewAdapter();
        modifyRecyclerViewOnClick();
    }

    void modifyRecyclerViewAdapter(){
        sonVideoCutsAdapter = new SonVideoCutsAdapter(mViewModel.getSonVideoCuts(),mViewModel.getVideoProject().getVideoNodeList(),mViewModel.getVideoNode());
        AlphaInAnimationAdapter alphaAnimationAdapter = new AlphaInAnimationAdapter(sonVideoCutsAdapter);
        alphaAnimationAdapter.setDuration(1000);
        alphaAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaAnimationAdapter.setFirstOnly(false);
        mBinding.recyclerView.setAdapter(alphaAnimationAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                int targetNodeIndex = mViewModel.getVideoNode().getSons().get(position);
                //移除此子节点
                mViewModel.getVideoNode().getSons().remove((Integer)targetNodeIndex);
                //如果子节点已经没有任何父亲则移动到孤立
                deleteNode(targetNodeIndex,mViewModel.getVideoNode().getIndex());
            }
            @Override
            public void onClick(View v, int position) {
                //保存子节点
                saveVideoCutsToCurrentNode();
                //设置新父亲
                mViewModel.setCurrentNode(mViewModel.getVideoNode().getSons().get(position));
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