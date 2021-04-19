package com.example.contact_client.project_creator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contact_client.R;
import com.example.contact_client.databinding.FragmentConditionBinding;
import com.example.contact_client.project_creator.Condition.Condition;
import com.example.contact_client.project_creator.CreatorViewModel;
import com.example.contact_client.project_creator.VideoNode;
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
    Condition defaultCondition;

    public ConditionFragment() {
        // Required empty public constructor
        defaultCondition = new Condition("请单击选择",-1,-1,-1);
    }

    public static ConditionFragment newInstance(CreatorViewModel viewModel) {
        ConditionFragment fragment = new ConditionFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_condition,container,false);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        mViewModel.setCondition(defaultCondition);
        conditionAdapter = new ConditionAdapter(mViewModel.getVideoNode(),mViewModel.getVideoProject().getConditions());
        conditionAdapter.setOnClick(new ConditionAdapter.onClick() {
            @Override
            public void onClick(View v, int position) {
                mViewModel.setCondition(conditionAdapter.getConditionList().get(position));
            }
        });
        binding.recyclerView2.setAdapter(conditionAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.buttonAddCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Condition condition = new Condition();
                conditionAdapter.addCondition(condition);
            }
        });
        binding.conditionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = conditionAdapter.getConditionList().indexOf(mViewModel.getConditionMutableLiveData().getValue());
                if(index>=0){
                    conditionAdapter.getConditionList().remove(index);
                    conditionAdapter.notifyItemRemoved(index);
                    mViewModel.setCondition(defaultCondition);
                }
            }
        });
        binding.conditionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.conditionName.getEditableText().toString().isEmpty()){
                    String newName = binding.conditionName.getEditableText().toString();
                    if(!newName.equals(""))
                        mViewModel.getCondition().setConditionName(newName);
                }
                if(!binding.conditionMin.getEditableText().toString().isEmpty()){
                    int min = Integer.parseInt(binding.conditionMin.getEditableText().toString().trim());
                    mViewModel.getCondition().setMin(min);
                }
                if(!binding.conditionMax.getEditableText().toString().isEmpty()){
                    int max = Integer.parseInt(binding.conditionMax.getEditableText().toString().trim());
                    mViewModel.getCondition().setMax(max);
                }
                if(!binding.conditionDefault.getEditableText().toString().isEmpty()){
                    int defaultValue = Integer.parseInt(binding.conditionDefault.getEditableText().toString().trim());
                    mViewModel.getCondition().setDefaultValue(defaultValue);
                }
                conditionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getVideoNodeMutableLiveData().observe(this, new Observer<VideoNode>() {
            @Override
            public void onChanged(VideoNode videoNode) {
                conditionAdapter.changeNode(videoNode);
            }
        });
        mViewModel.getConditionMutableLiveData().observe(this, new Observer<Condition>() {
            @Override
            public void onChanged(Condition condition) {
                binding.conditionName.setText(condition.getConditionName());
                binding.conditionDefault.setText(String.valueOf(condition.getDefaultValue()));
                binding.conditionMax.setText(String.valueOf(condition.getMax()));
                binding.conditionMin.setText(String.valueOf(condition.getMin()));
            }
        });
    }
}