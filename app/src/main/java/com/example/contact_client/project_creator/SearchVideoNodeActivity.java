package com.example.contact_client.project_creator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.project_creator.adapters.SearchVideoNodeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchVideoNodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_get);
        List<VideoNode> videoNodeList = getIntent().getParcelableArrayListExtra(getString(R.string.videoNodes));
        Log.d("mylo",videoNodeList.toString());
        //绑定recyclerView
        RecyclerView recyclerView = findViewById(R.id.getRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchVideoNodeAdapter myAdapter = new SearchVideoNodeAdapter();
        recyclerView.setAdapter(myAdapter);
        myAdapter.setVideoNodeList(videoNodeList);
        myAdapter.notifyDataSetChanged();
        //设置button
        Button button = findViewById(R.id.buttonDecide);
        button.setOnClickListener(v -> {
            ArrayList<Integer> list = new ArrayList<>();
            Map<Integer,Boolean> checkStatus = myAdapter.getCheckStatus();
            for(int i=0;i<myAdapter.getItemCount();i++){
                //如果某个位置上的条目被选中，则添加
                try {
                    if(checkStatus.get(i)){
                        list.add(myAdapter.getVideoNodeList().get(i).getIndex());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.d("mylo","send data index: "+list.toString());
            Intent intent = new Intent();
            intent.putIntegerArrayListExtra(getString(R.string.videoNodeIndexes), list);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

}