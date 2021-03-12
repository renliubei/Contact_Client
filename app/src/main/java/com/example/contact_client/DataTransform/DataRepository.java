package com.example.contact_client.DataTransform;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class DataRepository {
    private final static String IP = "http://10.251.253.66:8080";

    private final static String USER_API = "/api/user";
    private final static String REGISTER = "/register?";
    private final static String LOGIN = "/login?";

    private final static String REGISTER_USER = IP + USER_API + REGISTER;
    private final static String LOGIN_USER = IP + USER_API + LOGIN;

    private Context context;

    public DataRepository(Context context) {
        this.context = context;
    }

    /*
    part of registering
     */

    /*
    @param: password :
    @param: mobile :
    @param: username :
    @param: sex :
    @param: registerCallBack :
     */
    public void postRegister(String password, String mobile, String username, int sex, RegisterCallBack registerCallBack){
        String url = REGISTER_USER + "password=" + password + "&mobile=" + mobile + "&username=" + username + "&sex=" + sex;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("me", "onResponse: " + response);
                    RegisterData registerData = new Gson().fromJson(response, RegisterData.class);
                    if (registerData.getCode() == 200){
                        registerCallBack.onRegister(Status.CONNECT_SUCCESS);
                    }else if (registerData.getCode() == 503){
                        registerCallBack.onRegister(Status.TEL_OCCUPIED);
                    }else registerCallBack.onRegister(Status.UNKNOWN);
                },
                error -> {
                    Log.d("me", "onErrorResponse: " + error);
                    registerCallBack.onRegister(Status.NETWORK_FAIL);
                });

        VolleySingleton.getInstance(context).getQueue().add(stringRequest);
    }

    public interface RegisterCallBack{
        void onRegister(int status);
    }


    /*
    part of login
     */

    /*
    @param: mobile :
    @param: password :
    @param: loginCallBack :
     */
    public void postLogin(String mobile, String password, LoginCallBack loginCallBack){
        String url = LOGIN_USER + "mobile=" + mobile + "&password=" + password;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("me", "onResponse: " + response);
                    LoginData loginData = new Gson().fromJson(response, LoginData.class);
                    if (loginData.getCode() == 200){
                        loginCallBack.onUserInfo(loginData);
                        loginCallBack.onLogin(Status.CONNECT_SUCCESS);
                    }else if (loginData.getCode() == 503){
                        loginCallBack.onLogin(Status.TEL_WRONG);
                    }else {
                        loginCallBack.onLogin(Status.UNKNOWN);
                    }
                },
                error -> {
                    Log.d("me", "onErrorResponse: " + error);
                    loginCallBack.onLogin(Status.NETWORK_FAIL);
                }
        );

        VolleySingleton.getInstance(context).getQueue().add(stringRequest);
    }

    public interface LoginCallBack{
        void onUserInfo(LoginData loginData);
        void onLogin(int status);
    }
}
