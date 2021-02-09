package com.example.contact_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.contact_client.databinding.ActivityMainBinding;
import com.example.contact_client.databinding.FactoryFragmentBinding;

public class MainActivity extends AppCompatActivity {
    FactoryFragmentBinding factoryFragmentBinding;
    ActivityMainBinding activityMainBinding;
    UserViewModel userViewModel;
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


    }
}