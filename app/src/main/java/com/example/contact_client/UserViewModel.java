package com.example.contact_client;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    //管理用户界面的账号，用户名和头像资源
    // TODO: Implement the ViewModel
    private MutableLiveData<String> name, account,password;
    private MutableLiveData<Long> iconResource;

    public MutableLiveData<String> getName() {
        if(name==null){
            name = new MutableLiveData<>();
            name.setValue("未登录");
        }
        return name;
    }

    public void setName(MutableLiveData<String> name) {
        this.name = name;
    }

    public MutableLiveData<String> getAccount() {
        if(account ==null){
            account = new MutableLiveData<>();
            account.setValue("请先登录");
        }
        return account;
    }

    public void setAccount(MutableLiveData<String> account) {
        this.account = account;
    }

    public MutableLiveData<Long> getIconResource() {
        return iconResource;
    }

    public void setIconResource(MutableLiveData<Long> iconResource) {
        this.iconResource = iconResource;
    }
}