package com.example.contact_client.DataTransform;


import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("code")
    private Integer code;

    @SerializedName("msg")
    private String message;

    @SerializedName("data")
    private ReturnData returnData;

    @SerializedName("timestamp")
    private String timeStamp;

    public Integer getCode() {
        return code;
    }

    public ReturnData getReturnData() {
        return returnData;
    }

    public class ReturnData{
        private String id;
        private String password;
    }

}
