package com.example.contact_client.project_manager;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact_client.R;
import com.example.contact_client.databinding.FragmentProjectEditorBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectEditorFragment extends Fragment {

    private ProjectViewModel mViewModel;
    private FragmentProjectEditorBinding binding;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectEditorFragment newInstance(String param1, String param2) {
        ProjectEditorFragment fragment = new ProjectEditorFragment();
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_editor,container,false);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
        mViewModel.getEditorHintDecs().observe(this, s -> binding.projectDescEditor.setHint(s));
        mViewModel.getEditorHintName().observe(this, s -> binding.projectNameEditor.setHint(s));
        mViewModel.getHintCover().observe(this, s -> {
            if(s==null){
                Glide.with(getContext())
                        .load(R.drawable.defualt_project_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(binding.imageView2);
            }else{
                Glide.with(getContext())
                        .load(Uri.fromFile(new File(s)))
                        .error(R.drawable.defualt_project_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(binding.imageView2);
            }
        });
    }

}