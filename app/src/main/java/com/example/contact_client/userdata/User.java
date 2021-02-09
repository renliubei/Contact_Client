package com.example.contact_client.userdata;

public class User{
    protected String name;
    protected String tel;
    protected String password;
    protected int sex; // 1 for male ; 2 for female ; 0 for unknown

    public User(String name, String tel, String password, int sex) {
        this.name = name;
        this.tel = tel;
        this.password = password;
        this.sex = sex;
    }

    public User(String tel, String password) {
        this.tel = tel;
        this.password = password;
        this.name = "";
        this.sex = 0;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getPassword() {
        return password;
    }
}
