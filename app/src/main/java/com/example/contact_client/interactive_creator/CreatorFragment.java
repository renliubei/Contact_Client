package com.example.contact_client.interactive_creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityInteracitveCreatorBinding;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatorFragment extends Fragment {
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatorFragment newInstance(String param1, String param2) {
        CreatorFragment fragment = new CreatorFragment();
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
        return inflater.inflate(R.layout.activity_interacitve_creator, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }
}