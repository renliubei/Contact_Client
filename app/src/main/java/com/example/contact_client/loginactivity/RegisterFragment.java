package com.example.contact_client.loginactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.contact_client.R;
import com.example.contact_client.userdata.UserInit;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private Button button;
    private EditText telText, nameText, pwdText, checkPwdText;
    private RadioGroup sexRadio;
    private String tel, name, pwd, checkPwd;
    private int sex;
    private UserInit userInfo;
    private ArrayList<String> res;
    private String s = "abab";

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                userInfo = new UserInit(name, tel, pwd, sex);
                res = userInfo.checkRegisterInfo(checkPwd);
                if (Boolean.valueOf(res.get(1))) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_registerFragment_to_loginFragment2);
                }
                Toast.makeText(getActivity(), res.get(0)+"!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInfo(){
        tel = telText.getText().toString().trim();
        name = nameText.getText().toString().trim();
        pwd = pwdText.getText().toString().trim();
        checkPwd = checkPwdText.getText().toString().trim();
    }
}