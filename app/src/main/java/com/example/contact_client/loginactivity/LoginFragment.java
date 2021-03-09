package com.example.contact_client.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.contact_client.MainActivity;
import com.example.contact_client.R;
import com.example.contact_client.userdata.UserInit;

public class LoginFragment extends Fragment {
    private EditText telText, pwdText;
    private Button loginButton;
    private TextView registerText, forgetPwdText;
    private String tel, pwd;
    private UserInit userInfo;

    public LoginFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        telText = getView().findViewById(R.id.editTextPhone);
        pwdText = getView().findViewById(R.id.editPassword);

        loginButton = getView().findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = telText.getText().toString().trim();
                pwd = pwdText.getText().toString().trim();
                userInfo = new UserInit(tel, pwd);
                // TODO : complete here
                if (true){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("account",account);
//                    bundle.putString("password",password);
//                    intent.putExtra("userdata",bundle);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "登录成功!", Toast.LENGTH_LONG).show();
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
