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

    private final static String REGISTER_USER = IP + USER_API + REGISTER;

    private Context context;

    public DataRepository(Context context) {
        this.context = context;
    }

    public void postRegister(String password, String mobile, String username, int sex, RegisterCallBack registerCallBack){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                REGISTER_USER + "password=" + password + "&mobile=" + mobile + "&username=" + username + "&sex=" + sex,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("me", "onResponse: " + response);
                        RegisterData registerData = new Gson().fromJson(response, RegisterData.class);
                        if (registerData.getCode() == 200){
                            registerCallBack.onRegister(Status.CONNECT_SUCCESS);
                        }else if (registerData.getCode() == 503){
                            registerCallBack.onRegister(Status.TEL_OCCUPIED);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerCallBack.onRegister(Status.NETWORK_FAIL);
                    }
                });
        VolleySingleton.getInstance(context).getQueue().add(stringRequest);
    }

    public interface RegisterCallBack{
        void onRegister(int status);
    }
}
