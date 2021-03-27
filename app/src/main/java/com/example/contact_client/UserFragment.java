package com.example.contact_client;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact_client.Utils.BitmapUtils;
import com.example.contact_client.Utils.CameraUtils;
import com.example.contact_client.Utils.SPUtils;
import com.example.contact_client.aboutcontact_activity.AboutContactActivity;
import com.example.contact_client.loginactivity.InitActivity;
import com.example.contact_client.myaccount_activity.MyAccountActivity;
import com.example.contact_client.mywork_activity.MyWorkActivity;
import com.example.contact_client.versiondetail_activity.VersionDetailActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {
    // 权限请求
    private RxPermissions rxPermissions;
    private boolean hasPermissions = false;

    // 头像
    private View view;
    private ImageView mFront;
    private ImageView mBack;
    private String base64Pic; // Base64
    private Bitmap orc_bitmap; // 图片的bitmap

    // 功能按钮
    private ButtonView myAccount;
    private ButtonView myWork;
    private ButtonView mAbout;
    private ButtonView mVersion;
    private ButtonView mLogout;

    // 底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    // 弹窗视图
    private View bottomView;


    // 存储拍照完的照片
    private File outputImagePath;

    // 启动相机
    public static final int TAKE_PHOTO = 1;
    // 启动相册
    public static final int SELECT_PHOTO = 2;

    // Glide请求图片配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 不做磁盘缓存
            .skipMemoryCache(true); // 不做内存缓存
    // 背景磨砂
    MultiTransformation multi = new MultiTransformation(
            // 高斯模糊，第一个为模糊度0~25，第二个为取样（暂时不知道有啥用）
            new BlurTransformation(25, 3),
            // 缩放图片填充满ImageView并裁掉多出的部分
            new CenterCrop()
    );

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel

        initView();

        // 点击头像换头像
        mFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                if (hasPermissions) {
                    changeAvatar(view);
                }
            }
        });

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

        // 圆形图形
        Glide.with(this).load(R.drawable.icon).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mFront);
        Glide.with(this).load(R.drawable.icon)
                // 高版本Glide得用apply
                .apply(RequestOptions.bitmapTransform(multi))
                .into(mBack);

        // item控件
        myAccount = getView().findViewById(R.id.myaccount);
        myWork = getView().findViewById(R.id.mywork);
        mAbout = getView().findViewById(R.id.about);
        mVersion = getView().findViewById(R.id.version);
        mLogout = getView().findViewById(R.id.logout);
        // 退出按钮箭头不显示
        mLogout.setShowRightArrow(false);

        String imageUrl = SPUtils.getString("imageUrl", null, getActivity());
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).apply(requestOptions).into(mFront);
            Glide.with(this).load(imageUrl)
                    // 高版本Glide得用apply
                    .apply(RequestOptions.bitmapTransform(multi))
                    .into(mBack);
        }
    }

    // 更换头像
    public void changeAvatar(View view){
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

        // 拍照
        tvTakePictures.setOnClickListener(v->{
            takePhoto();
            showMsg("拍照");
            bottomSheetDialog.cancel();
        });
        // 打开相册
        tvOpenAlbum.setOnClickListener(v->{
            openAlbum();
            showMsg("打开相册");
            bottomSheetDialog.cancel();
        });
        // 取消
        tvCancel.setOnClickListener(v->{
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    // 判断手机系统版本
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        // 4.4及以上处理图片的方法
                        imagePath = CameraUtils.getImageOnKitKatPath(data, getActivity());
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, getActivity());
                    }
                    // 显示图片
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            // 放入缓存
            SPUtils.putString("imageUrl", imagePath, getActivity());
            // 显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(mFront);
            // 背景磨砂
            MultiTransformation multi = new MultiTransformation(
                    // 高斯模糊，第一个为模糊度0~25，第二个为取样（暂时不知道有啥用）
                    new BlurTransformation(25, 3),
                    // 缩放图片填充满ImageView并裁掉多出的部分
                    new CenterCrop()
            );
            Glide.with(this).load(imagePath)
                    // 高版本Glide得用apply
                    .apply(RequestOptions.bitmapTransform(multi))
                    .into(mBack);
            // 压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            // 转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);
        } else {
            showMsg("图片获取失败");
        }
    }

    // 拍照
    private void takePhoto() {
         SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
         String filename = timeStampFormat.format(new Date());
         outputImagePath = new File(Environment.getExternalStorageDirectory(),
                filename + ".jpg");
         Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(getContext(), outputImagePath);
         //开启一个带返回值的Activity，请求码为TAKE_PHOTO
         startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    // 打开相册
    private void openAlbum() {
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    // 检查权限
    private void checkPermission() {
        if (hasPermissions)
            return;
        rxPermissions = new RxPermissions(getActivity());
        // 权限请求
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted->{
                    if(granted){
                        // 申请成功
                        showMsg("已获取权限");
                        hasPermissions = true;
                    } else {
                        // 申请失败
                        showMsg("权限未开启");
                        hasPermissions = false;
                    }
                });
    }

    // Toast提示
    private void showMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}