package com.example.contact_client.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.MainActivity;
import com.example.contact_client.R;
import com.example.contact_client.UserViewModel;
import com.example.contact_client.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        activityLoginBinding.setUserData(userViewModel);
        activityLoginBinding.setLifecycleOwner(this);
        //设置登录动作

        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account,password;
                account = activityLoginBinding.editTextNumber.getText().toString();
                password = activityLoginBinding.editTextTextPassword.getText().toString();
                if(!account.isEmpty()&!password.isEmpty()){
                    //bundle+intent启动新活动并传递数据
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    //尝试在Activity之间用viewmodel失败，有待研究
//                    userViewModel.getAccount().setValue(account);
//                    userViewModel.getName().setValue("Antonio");
                    Bundle bundle = new Bundle();
                    bundle.putString("account",account);
                    bundle.putString("password",password);
                    intent.putExtra("userdata",bundle);
                    startActivity(intent);
                    //
                    Toast.makeText(v.getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(),"账号/密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}