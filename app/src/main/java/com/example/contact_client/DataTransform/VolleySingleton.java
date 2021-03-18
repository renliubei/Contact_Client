package com.example.contact_client.DataTransform;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton INSTANCE;
    private static Context context;
    private RequestQueue queue;

    private VolleySingleton(Context context){
        this.context = context;
        INSTANCE = null;
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new VolleySingleton(context);
        }
        return INSTANCE;
    }

    public RequestQueue getQueue(){
        if (queue == null){
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }
}
