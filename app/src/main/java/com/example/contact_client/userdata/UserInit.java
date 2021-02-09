package com.example.contact_client.userdata;

import java.util.ArrayList;

public class UserInit extends User{

    public UserInit(String name, String tel, String password, int sex) {
        super(name, tel, password, sex);
    }

    public UserInit(String tel, String password) {
        super(tel, password);
    }
/*
    register fragment
     */

    private Boolean checkTelOk(){
        return this.tel.length() == 11;
    }

    private Boolean checkTelUnique(){
        // TODO : check if tel is the same in database
        return true;
    }

    private Boolean checkPasswordOk(){
        return this.password.length() <= 16 && !this.password.equals("");
    }

    private Boolean checkPasswordRight(String checkingPassword){
        return this.password.equals(checkingPassword);
    }

    public ArrayList<String> checkRegisterInfo(String checkingPassword){
        /*
        res stores two Strings
        0 for the notice
        1 for the result
         */
        ArrayList<String> res = new ArrayList<>();
        Boolean flag = true;
        if (this.tel.equals("")){
            res.add("手机号不能为空");
            flag = false;
        }else if (!checkTelOk()){
            res.add("请正确输入手机号码");
            flag = false;
        }else if (!checkTelUnique()){
            res.add("手机号码已注册");
            flag = false;
        }else if (this.name.equals("")){
            res.add("用户名不能为空");
            flag = false;
        }else if (!checkPasswordOk()){
            res.add("请输入6至16位密码");
            flag = false;
        }else if (!checkPasswordRight(checkingPassword)){
            res.add("两次输入密码不一致");
            flag = false;
        }
        if (flag) {
            res.add("注册成功");
            res.add("true");
        }else {
            res.add("false");
        }

        return res;
    }

    /*
    login fragment
     */

    public ArrayList<String> checkLoginInfo(){
        /*
        res stores two Strings
        0 for the notice
        1 for the result
         */
        ArrayList<String> res = new ArrayList<>();
        // TODO: check info from database

        return res;
    }

    public void saveInDatabase(){
        // TODO : save in database
    }
}
