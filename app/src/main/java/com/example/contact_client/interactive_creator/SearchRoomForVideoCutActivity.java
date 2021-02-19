package com.example.contact_client.interactive_creator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.video_manager.VideoCutsViewModel;

import java.util.List;

public class SearchRoomForVideoCutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoCutsViewModel videoCutsViewModel;
    private SonVideoCutAdapter myAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_get_video);
        videoCutsViewModel = new ViewModelProvider(this).get(VideoCutsViewModel.class);
        //绑定recyclerView
        recyclerView = findViewById(R.id.getVideoCutRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new SonVideoCutAdapter();
        recyclerView.setAdapter(myAdapter);
        //设置button
        button = findViewById(R.id.buttonDecide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"hello?",Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoCutsViewModel.getAllLiveDataVideoCuts().observe(this, new Observer<List<VideoCut>>() {
            @Override
            public void onChanged(List<VideoCut> videoCuts) {
                myAdapter.setAllVideoCuts(videoCuts);
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}