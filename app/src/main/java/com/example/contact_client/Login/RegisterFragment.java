package com.example.contact_client.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.contact_client.DataTransform.DataRepository;
import com.example.contact_client.DataTransform.Status;
import com.example.contact_client.R;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {
    private Button button;
    private EditText telText, nameText, pwdText, checkPwdText;
    private RadioGroup sexRadio;
    private String tel, name, pwd, checkPwd;
    private int sex;
    private ArrayList<String> res;

    private DataRepository registerRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerRepository = new DataRepository(getContext());

        button = getView().findViewById(R.id.registerButton);
        telText = getView().findViewById(R.id.editTextPhone2);
        nameText = getView().findViewById(R.id.editTextName);
        pwdText = getView().findViewById(R.id.editTextPassword);
        checkPwdText = getView().findViewById(R.id.editPasswordCheck);
        sexRadio = getView().findViewById(R.id.sexRadioGroup);

        sexRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO: find how to use switch
                if (checkedId == R.id.radioMale){
                    sex = 1;
                }else if (checkedId == R.id.radioFemale){
                    sex = 2;
                }else{
                    sex = 0;
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                res = checkRegisterInfo(name, tel, pwd, checkPwd);
                if (Boolean.valueOf(res.get(1))) {

                    registerRepository.postRegister(pwd, tel, name, sex, new DataRepository.RegisterCallBack() {
                        @Override
                        public void onRegister(int status) {
                            switch (status){
                                case Status.CONNECT_SUCCESS:
                                    Toast.makeText(getActivity(),"注册成功!",Toast.LENGTH_SHORT).show();
                                    NavController navController = Navigation.findNavController(getView());
                                    navController.navigate(R.id.action_registerFragment_to_loginFragment2);
                                    break;
                                case Status.TEL_OCCUPIED:
                                    Toast.makeText(getActivity(),"该手机号已注册!",Toast.LENGTH_SHORT).show();
                                    break;
                                case Status.NETWORK_FAIL:
                                    Toast.makeText(getActivity(),"网络错误!",Toast.LENGTH_SHORT).show();
                                    break;
                                case Status.UNKNOWN:
                                    Toast.makeText(getActivity(),"？发生未知错误？",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });

                }else {
                    Toast.makeText(getActivity(), res.get(0)+"!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getInfo(){
        tel = telText.getText().toString().trim();
        name = nameText.getText().toString().trim();
        pwd = pwdText.getText().toString().trim();
        checkPwd = checkPwdText.getText().toString().trim();
    }

    private ArrayList<String> checkRegisterInfo(String name, String tel, String pwd, String checkPwd) {
        ArrayList<String> res = new ArrayList<>();

        boolean flag = true;
        if (tel.equals("")){
            res.add("手机号不能为空");
            flag = false;
        }else if (tel.length() != 11){
            res.add("请正确输入手机号码");
            flag = false;
        }else if (name.equals("")){
            res.add("用户名不能为空");
            flag = false;
        }else if (! (pwd.length() >= 6 && pwd.length() <= 16)){
            res.add("请输入6至16位密码");
            flag = false;
        }else if (! pwd.equals(checkPwd)){
            res.add("两次输入密码不一致");
            flag = false;
        }

        if (flag) {
            res.add("信息无误");
            res.add("true");
        }else {
            res.add("false");
        }

        return res;
    }

}