package com.example.contact_client.ProjectFactory;

import android.content.Context;

import com.example.contact_client.repository.VideoProject;

import java.util.HashMap;
import java.util.List;

public class VideoTree {
    private final Context context;
    private VideoProject videoProject;
    private HashMap<Integer, List<VideoLayout>> videoLayouts;
    private int lastLine = 0;
    private int total = 0;

    public VideoTree(Context context, VideoProject videoProject) {
        this.context = context;
        this.videoProject = videoProject;

        if (videoProject == null){
            videoLayouts = new HashMap<>();
        }else {
            toTree(videoProject);
        }
    }

    private void toTree(VideoProject videoProject){
        //TODO: BFS

        new VideoLayout(context, FactoryConstant.NormalCell, total, -1){
            @Override
            public void setAction() {
                super.setAction();

            }
        };
    }

    public VideoProject toProject(){
        //TODO: fill in this transform function
        return videoProject;
    }
}
