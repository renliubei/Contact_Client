package com.example.contact_client;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.contact_client.databinding.FactoryFragmentBinding;

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

        return factoryFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FactoryViewModel.class);
        // TODO: Use the ViewModel
    }

}