package com.example.contact_client.ProjectFactory;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoProject;

public class ProjectFactory extends Fragment {
    private VideoProject videoProject;
    private VideoTree videoTree;
    private VideoFactoryManager manager;

    private ConstraintLayout constraintLayout;
    private DrawLine drawLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_factory, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        constraintLayout = getView().findViewById(R.id.factoryConstraintLayout);
        drawLine = getView().findViewById(R.id.backgroundLine);

        //TODO: change to Parceblable
        if (getArguments() != null){
            Bundle bundle = getArguments();
            videoProject = (VideoProject) bundle.get("project");
        }

        videoTree = new VideoTree(getActivity(), videoProject);

        manager = new VideoFactoryManager(getActivity(), constraintLayout, videoTree, drawLine);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}