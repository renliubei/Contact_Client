package com.example.contact_client.project_manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.contact_client.R;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;

import static com.example.contact_client.project_manager.VideoProjectActivity.IMAGE;

public class takePhotoActivity extends TakePhotoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onPickFromGallery();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Intent intent = new Intent();
        intent.putExtra(IMAGE,result.getImage());
        setResult(RESULT_OK,intent);
        Log.d("mylo","send image data: "+result.getImage().getOriginalPath());
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        setResult(RESULT_CANCELED);
        finish();
    }

}