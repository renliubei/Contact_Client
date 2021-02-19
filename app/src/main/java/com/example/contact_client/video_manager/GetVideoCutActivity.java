package com.example.contact_client.video_manager;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class GetVideoCutActivity extends AppCompatActivity {

    private final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    private List<VideoCut> videoCutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_get_video);
//        Toast.makeText(this, "successfully open GetVideoCut", Toast.LENGTH_SHORT).show();

        //获取本地视频列表
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            videoCutList = getLocalVideoCut();
//            Log.d("mylo", videoCutList.toString());
            //设置视图
            RecyclerView recyclerView = findViewById(R.id.getVideoCutRecycleView);
            VideoGalleryAdapter videoGalleryAdapter = new VideoGalleryAdapter();
            videoGalleryAdapter.setAllVideoCuts(videoCutList);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(videoGalleryAdapter);
        }
        //
        Button button = findViewById(R.id.buttonDecide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VideoCut> returnList = new ArrayList<VideoCut>();
                for (int i = 0; i < videoCutList.size(); i++) {
                    if (videoCutList.get(i).isCut()) {
                        returnList.add(videoCutList.get(i));
                    }
                }
                Log.d("mylo", "returnList is \n" + returnList.toString());
                //返回获取到的数据
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoCuts", (ArrayList<? extends Parcelable>) returnList);
                Intent intent = new Intent();
                intent.putExtra("videoCuts", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    videoCutList = getLocalVideoCut();
//                    Log.d("mylo", videoCutList.toString());
                    //设置视图
                    RecyclerView recyclerView = findViewById(R.id.getVideoCutRecycleView);
                    VideoGalleryAdapter videoGalleryAdapter = new VideoGalleryAdapter();
                    videoGalleryAdapter.setAllVideoCuts(videoCutList);
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    recyclerView.setAdapter(videoGalleryAdapter);
                } else {
                    Toast.makeText(this, "请您在设置中允许存储权限", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //获取本地视频，要先申请权限
    private List<VideoCut> getLocalVideoCut() {
        // Need the READ_EXTERNAL_STORAGE permission if accessing video files that your app didn't create.

        // Container for information about each video.
        List<VideoCut> list = new ArrayList<>();
        // args for query
        String[] projection = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Media._ID,
        };
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String videoThumbnailPath = cursor.getString(dataColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                VideoCut videoCut = new VideoCut(false, "VideoCut" + id, "Is a VideoCut", contentUri.toString(), videoThumbnailPath);
                videoCut.setId(id);
                list.add(videoCut);
            }
        }
        return list;
    }
}