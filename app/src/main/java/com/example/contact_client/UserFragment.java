package com.example.contact_client;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact_client.aboutcontact_activity.AboutContactActivity;
import com.example.contact_client.Login.InitActivity;
import com.example.contact_client.myaccount_activity.MyAccountActivity;
import com.example.contact_client.mywork_activity.MyWorkActivity;
import com.example.contact_client.versiondetail_activity.VersionDetailActivity;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class UserFragment extends Fragment {
    private View view;
    private ImageView mFront;
    private ImageView mBack;

    private ButtonView myAccount;
    private ButtonView myWork;
    private ButtonView mAbout;
    private ButtonView mVersion;
    private ButtonView mLogout;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //绑定binding和viewmodel
        //binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false);
        //mViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        //binding.setUserData(mViewModel);
        //binding.setLifecycleOwner(getActivity());
        //
        //private UserViewModel mViewModel;
        //UserFragmentBinding binding;

        view = inflater.inflate(R.layout.user_fragment, container, false);

        //return binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel

        initView();
        // 背景磨砂
        MultiTransformation multi = new MultiTransformation(
                // 高斯模糊，第一个为模糊度0~25，第二个为取样（暂时不知道有啥用）
                new BlurTransformation(25, 3),
                // 缩放图片填充满ImageView并裁掉多出的部分
                new CenterCrop()
        );
        Glide.with(this).load(R.drawable.icon)
                // 高版本Glide得用apply
                .apply(RequestOptions.bitmapTransform(multi))
                .into(mBack);
        // 圆形图形
        Glide.with(this).load(R.drawable.icon).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mFront);

        // user界面ButtonView跳转
        myAccount.setItemClickListener(new ButtonView.itemClickListener() {
            @Override
            public void itemClick() {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }
        });

        myWork.setItemClickListener(new ButtonView.itemClickListener() {
            @Override
            public void itemClick() {
                Intent intent = new Intent(getActivity(), MyWorkActivity.class);
                startActivity(intent);
            }
        });

        mAbout.setItemClickListener(new ButtonView.itemClickListener() {
            @Override
            public void itemClick() {
                Intent intent = new Intent(getActivity(), AboutContactActivity.class);
                startActivity(intent);
            }
        });

        mVersion.setItemClickListener(new ButtonView.itemClickListener() {
            @Override
            public void itemClick() {
                Intent intent = new Intent(getActivity(), VersionDetailActivity.class);
                startActivity(intent);
            }
        });

        mLogout.setItemClickListener(new ButtonView.itemClickListener() {
            @Override
            public void itemClick() {
                Intent intent = new Intent(getActivity(), InitActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initView() {
        // 头像控件
        mFront = getView().findViewById(R.id.icon_front);
        mBack = getView().findViewById(R.id.icon_back);

        // item控件
        myAccount = getView().findViewById(R.id.myaccount);
        myWork = getView().findViewById(R.id.mywork);
        mAbout = getView().findViewById(R.id.about);
        mVersion = getView().findViewById(R.id.version);
        mLogout = getView().findViewById(R.id.logout);
        // 退出按钮箭头不显示
        mLogout.setShowRightArrow(false);
    }
}