package com.example.contact_client;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.contact_client.databinding.UserFragmentBinding;

public class UserFragment extends Fragment {
    private UserViewModel mViewModel;
    UserFragmentBinding binding;
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //绑定binding和viewmodel
        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false);
        mViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        binding.setUserData(mViewModel);
        binding.setLifecycleOwner(getActivity());
        //
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}