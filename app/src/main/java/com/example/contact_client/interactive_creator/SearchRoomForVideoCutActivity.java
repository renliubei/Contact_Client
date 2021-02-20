package com.example.contact_client.interactive_creator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.video_manager.VideoCutsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchRoomForVideoCutActivity extends AppCompatActivity {
    private VideoCutsViewModel videoCutsViewModel;
    private SearchRoomVideoCutAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_get_video);
        videoCutsViewModel = new ViewModelProvider(this).get(VideoCutsViewModel.class);
        //绑定recyclerView
        RecyclerView recyclerView = findViewById(R.id.getVideoCutRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new SearchRoomVideoCutAdapter();
        recyclerView.setAdapter(myAdapter);
        //设置button
        Button button = findViewById(R.id.buttonDecide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VideoCut> list = new ArrayList<>();
                Map<Integer,Boolean> checkStatus = myAdapter.getCheckStatus();
                for(int i=0;i<myAdapter.getItemCount();i++){
                    //如果某个位置上的条目被选中，则添加
                    try {
                        if(checkStatus.get(i)){
                            list.add(myAdapter.getAllVideoCuts().get(i));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Log.d("mylo","send data: "+list.toString());
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("videoCuts", (ArrayList<? extends Parcelable>) list);
                setResult(RESULT_OK, intent);
                finish();
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