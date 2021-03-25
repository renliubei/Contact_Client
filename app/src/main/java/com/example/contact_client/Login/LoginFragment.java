package com.example.contact_client.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.contact_client.DataTransform.DataRepository;
import com.example.contact_client.DataTransform.LoginData;
import com.example.contact_client.DataTransform.Status;
import com.example.contact_client.MainActivity;
import com.example.contact_client.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginFragment extends Fragment {
    private EditText telText, pwdText;
    private FancyButton loginButton;
    private TextView registerText, forgetPwdText;
    private String tel, pwd;

    private DataRepository loginRepository;
    private Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginRepository = new DataRepository(getContext());

        telText = getView().findViewById(R.id.editTextPhone);
        pwdText = getView().findViewById(R.id.editPassword);

        loginButton = getView().findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = telText.getText().toString().trim();
                pwd = pwdText.getText().toString().trim();

                if (tel.length() != 11){
                    Toast.makeText(getActivity(), "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                }else if (pwd.equals("")){
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                }else{
                    loginRepository.postLogin(tel, pwd, new DataRepository.LoginCallBack() {
                        @Override
                        public void onUserInfo(LoginData loginData) {
                            bundle = new Bundle();
                            bundle.putString("name", loginData.getReturnData().getUsername());
                            bundle.putString("id", loginData.getReturnData().getId());
                            bundle.putString("phone", tel);
                        }

                        @Override
                        public void onLogin(int status) {
                            switch (status){
                                case Status.CONNECT_SUCCESS:
                                    if (bundle != null){
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.putExtra("userData", bundle);
                                        startActivity(intent);
                                        Toast.makeText(getActivity(), "登录成功!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(),"？发生未知错误？",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case Status.NETWORK_FAIL:
                                    Toast.makeText(getActivity(),"网络错误!",Toast.LENGTH_SHORT).show();
                                    break;
                                case Status.TEL_WRONG:
                                    Toast.makeText(getActivity(), "密码错误或账号已删除", Toast.LENGTH_SHORT).show();
                                    break;
                                case Status.UNKNOWN:
                                    Toast.makeText(getActivity(),"？发生未知错误？",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
            }
        });


        registerText = getView().findViewById(R.id.register_text);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_loginFragment2_to_registerFragment);
            }
        });

        forgetPwdText = getView().findViewById(R.id.forgetPassword);
        forgetPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : finish the fragment for changing password
            }
        });
    }
}
