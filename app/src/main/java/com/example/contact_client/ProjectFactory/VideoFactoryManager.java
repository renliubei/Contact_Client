package com.example.contact_client.ProjectFactory;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

public class VideoFactoryManager {
    private Context context;
    private ConstraintLayout constraintLayout;

    private VideoTree videoTree;
    private DrawLine drawLine;

    public VideoFactoryManager(Context context, ConstraintLayout constraintLayout, VideoTree videoTree, DrawLine drawLine) {
        this.context = context;
        this.constraintLayout = constraintLayout;
        this.videoTree = videoTree;
        this.drawLine = drawLine;
    }

    public void rearrange(){
        //TODO
    }

    private void setLine(){
        //TODO
    }
}
