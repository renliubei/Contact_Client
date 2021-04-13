package com.example.contact_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.contact_client.databinding.ActivityDigalogBinding;

/**
 * 返回需要填写的数值
 */
public class EditActivity extends AppCompatActivity {
    private String newName,newDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDigalogBinding binding;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_digalog);
        binding.setLifecycleOwner(this);
        binding.videoCutEditDecide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(!binding.videoCutEditFirst.getText().toString().isEmpty()){
                    newName = binding.videoCutEditFirst.getEditableText().toString();
                    intent.putExtra("newName",newName);
                }
                if(!binding.videoCutEditSecond.getText().toString().isEmpty()){
                    newDes = binding.videoCutEditSecond.getEditableText().toString();
                    intent.putExtra("newDes",newDes);
                }
                Log.d("mylo","new things are"+newDes+" "+newName);
                intent.putExtra("newName",newName);
                intent.putExtra("newDes",newDes);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.videoCutEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}