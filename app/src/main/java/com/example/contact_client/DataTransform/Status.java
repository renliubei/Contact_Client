package com.example.contact_client.DataTransform;

public interface Status {
    int UNKNOWN = -1;
    int NETWORK_FAIL = 0;
    int CONNECT_SUCCESS = 1;

    //Register
    int TEL_OCCUPIED = 2;
    //Login
    int TEL_UNKNOWN = 3;
}
