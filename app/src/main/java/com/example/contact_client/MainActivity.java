package com.example.contact_client;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.contact_client.databinding.ActivityMainBinding;
import com.example.contact_client.databinding.FactoryFragmentBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity {
    FactoryFragmentBinding factoryFragmentBinding;
    ActivityMainBinding activityMainBinding;
    UserViewModel userViewModel;

    // 权限请求
    private RxPermissions rxPermissions;
    // 是否拥有权限
    private boolean hasPermissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //相关绑定
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        //
//
//        //导航
        NavController navController = Navigation.findNavController(this,R.id.mainNavigation);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(activityMainBinding.bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView,navController);

        //取回登录数据
//        Bundle bundle = getIntent().getBundleExtra("userdata");
//        userViewModel.getAccount().setValue(bundle.getString("account"));
//        userViewModel.getName().setValue("Antonio");
        //

        // Log.d("MainActivity", "onCreat execute");
    }

    // 版本检查
    private void checkPermission(){
        // Android6.0及以上版本
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            rxPermissions = new RxPermissions(this);
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
        } else {
            // Android6.0以下
            showMsg("无需请求动态权限");
        }
    }

    // Toast提示
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}