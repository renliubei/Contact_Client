package com.example.contact_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.databinding.FactoryFragmentBinding;
import com.example.contact_client.interactive_creator.InteractiveCreatorActivity;
import com.example.contact_client.video_manager.VideoManagerActivity;

public class FactoryFragment extends Fragment {
    private FactoryFragmentBinding factoryFragmentBinding;
    private FactoryViewModel mViewModel;

    public static FactoryFragment newInstance() {
        return new FactoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        factoryFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.factory_fragment, container, false);
        factoryFragmentBinding.setLifecycleOwner(getActivity());

        factoryFragmentBinding.buttonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoManagerActivity.class);
                Toast.makeText(v.getContext(), "what's up?", Toast.LENGTH_SHORT);
                startActivity(intent);
            }
        });
        factoryFragmentBinding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InteractiveCreatorActivity.class);
                startActivity(intent);
            }
        });
        return factoryFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FactoryViewModel.class);
        // TODO: Use the ViewModel
    }

}