package com.example.contact_client.project_manager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.contact_client.R;
import com.example.contact_client.databinding.FragmentProjectGalleryBinding;
import com.example.contact_client.interactive_creator.InteractiveCreatorActivity;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectGalleryFragment extends Fragment {

    private ProjectViewModel mViewModel;
    private FragmentProjectGalleryBinding binding;
    private GalleryAdapter galleryAdapter;
    private BottomTextAdapter bottomTextAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectGalleryFragment newInstance(String param1, String param2) {
        ProjectGalleryFragment fragment = new ProjectGalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_gallery,container,false);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
        modifyRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getProjectsLiveDataList().observe(getActivity(), videoProjects -> {

            galleryAdapter.setVideoProjects(videoProjects);
            galleryAdapter.notifyDataSetChanged();

            bottomTextAdapter.setVideoProjects(videoProjects);
            bottomTextAdapter.notifyDataSetChanged();
        });
    }

    void modifyRecyclerView(){
        modifyProjectsRecyclerView();
        modifyBottomRecyclerView();
    }

    void modifyBottomRecyclerView(){
        bottomTextAdapter = new BottomTextAdapter();
        SlideInLeftAnimationAdapter slideInLeftAnimationAdapter = new SlideInLeftAnimationAdapter(bottomTextAdapter);
        slideInLeftAnimationAdapter.setDuration(1000);
        binding.recyclerViewBottomText.setLayoutManager(new ScrollSpeedLinearLayoutManger(getActivity()));
        binding.recyclerViewBottomText.setAdapter(slideInLeftAnimationAdapter);
        binding.recyclerViewBottomText.post(() -> bottomTextAdapter.setOnClickItem(new BottomTextAdapter.onClickItem() {
            @Override
            public void onClickDelete(View v, int position) {
                Toast.makeText(v.getContext(),"you click delete",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickDisplay(View v, int position) {
                Toast.makeText(v.getContext(),"you click display",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickEdit(View v, int position) {
                mViewModel.setPosition(position);
                startProjectCreator();
            }
        }));
    }

    void modifyProjectsRecyclerView(){
        galleryAdapter = new GalleryAdapter();
        //设置adapter动画
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(galleryAdapter);
        animationAdapter.setDuration(1500);
        animationAdapter.setInterpolator(new OvershootInterpolator());
        animationAdapter.setFirstOnly(false);
        //绑定adapter和layoutManager
        binding.recyclerViewProjects.setAdapter(animationAdapter);
        binding.recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //绑定snapHelper
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerViewProjects);
        //修改首尾item的margin
        binding.recyclerViewProjects.addItemDecoration(new MarginItemDecoration());
        //修改text的内容
        binding.recyclerViewProjects.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getChildCount() > 0 && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    try {
                        int position = recyclerView.getChildAdapterPosition(snapHelper.findSnapView(recyclerView.getLayoutManager()));
                        mViewModel.setPosition(position);
                        binding.recyclerViewBottomText.smoothScrollToPosition(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    void startProjectCreator(){
        if(mViewModel.getPosition()==-1){
            Toast.makeText(getActivity(),"无互动视频 or 请先滑动选中",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getActivity(), InteractiveCreatorActivity.class);
            intent.putExtra(getString(R.string.videoProject), galleryAdapter.getVideoProjects().get(mViewModel.getPosition()).getId());
            startActivity(intent);
        }
    }

    void startProjectEditor(){
        Intent intent = new Intent(getActivity(), takePhotoActivity.class);
        startActivity(intent);
    }

    class disScrollLinearLayoutManager extends LinearLayoutManager{
        private boolean isScrollEnabled = true;
        public disScrollLinearLayoutManager(Context context) {
            super(context);
        }
        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }
        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}