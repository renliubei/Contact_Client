package com.example.contact_client.project_creator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contact_client.R;
import com.example.contact_client.databinding.FragmentConditionBinding;
import com.example.contact_client.project_creator.Condition.Condition;
import com.example.contact_client.project_creator.CreatorViewModel;
import com.example.contact_client.project_creator.adapters.ConditionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConditionFragment extends Fragment {
    FragmentConditionBinding binding;
    CreatorViewModel mViewModel;
    ConditionAdapter conditionAdapter;
    Condition condition;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConditionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConditionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConditionFragment newInstance(String param1, String param2) {
        ConditionFragment fragment = new ConditionFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_condition,container,false);
        binding.setLifecycleOwner(getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(CreatorViewModel.class);
        init();
        return binding.getRoot();
    }

    private void init(){
        conditionAdapter = new ConditionAdapter(mViewModel.getVideoNode(),getActivity());
        conditionAdapter.setOnClick(new ConditionAdapter.onClick() {
            @Override
            public void onClick(View v, int position) {
                condition = conditionAdapter.getConditionList().get(position);
                binding.conditionName.setText(condition.getConditionName());
                binding.conditionDefault.setText(String.valueOf(condition.getDefaultValue()));
                binding.conditionMax.setText(String.valueOf(condition.getMax()));
                binding.conditionMin.setText(String.valueOf(condition.getMin()));
            }
        });
        binding.recyclerView2.setAdapter(conditionAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}