package com.example.contact_client.project_manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact_client.R;
import com.example.contact_client.databinding.FragmentProjectEditorBinding;
import com.example.contact_client.repository.VideoProject;

import java.io.File;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectEditorFragment extends Fragment {
    protected static final int SEARCH_PHOTO = 1;
    private ProjectViewModel mViewModel;
    private FragmentProjectEditorBinding binding;
    private onClickItem onClickItem;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
     * @return A new instance of fragment ProjectEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectEditorFragment newInstance(onClickItem onClickItem) {
        ProjectEditorFragment fragment = new ProjectEditorFragment();
        fragment.onClickItem = onClickItem;
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_editor,container,false);
        binding.setLifecycleOwner(getActivity());
        binding.imageViewProjectCover.setOnClickListener(v -> Toast.makeText(v.getContext(),"长按选择新图片",Toast.LENGTH_SHORT).show());
        binding.imageViewProjectCover.setOnLongClickListener(v -> {
            Toasty.info(getActivity().getApplicationContext(),"请选择新图片",Toasty.LENGTH_SHORT,true).show();
            Intent intent = new Intent(getActivity(),takePhotoActivity.class);
            getActivity().startActivityForResult(intent,SEARCH_PHOTO);
            return true;
        });

        binding.floatingActionButton.setOnClickListener(v -> {
            try{
                VideoProject videoProject = mViewModel.getProjectsLiveDataList().getValue().get(mViewModel.getPosition());
                if(!binding.projectNameEditor.getText().toString().isEmpty()) videoProject.setName(binding.projectNameEditor.getText().toString());
                if(!binding.projectDescEditor.getText().toString().isEmpty()) videoProject.setDescription(binding.projectDescEditor.getText().toString());
                if(mViewModel.getHintCover().getValue()!=null) videoProject.setCoverUrl(mViewModel.getHintCover().getValue());
                Log.d("mylo","this is new pro: "+videoProject.toString());
                compositeDisposable.add(
                        mViewModel.insertVideoProject(videoProject)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> Toasty.success(getContext(),"成功保存到id为"+aLong+"的互动视频",Toasty.LENGTH_SHORT,true).show(),
                                        Throwable::printStackTrace)
                );
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(v.getContext(),"请现在画廊中长按视频",Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
        mViewModel.getEditorHintDecs().observe(this, s -> {binding.projectDescEditor.setHint(s);binding.projectDescEditor.getText().clear();});
        mViewModel.getEditorHintName().observe(this, s -> {binding.projectNameEditor.setHint(s);binding.projectNameEditor.getText().clear();});
        mViewModel.getHintCover().observe(this, s -> {
            if(s==null){
                Glide.with(getActivity())
                        .load(R.drawable.defualt_project_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(binding.imageViewProjectCover);
            }else{
                Glide.with(getActivity())
                        .load(Uri.fromFile(new File(s)))
                        .error(R.drawable.defualt_project_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(binding.imageViewProjectCover);
            }
        });
        mViewModel.getHeadInfo().observe(this, s -> binding.textViewProjectEditiorHead.setText(s));
    }

    public interface onClickItem{
        void onClickSaveBtn();
    }
}