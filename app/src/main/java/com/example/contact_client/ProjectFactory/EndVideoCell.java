package com.example.contact_client.ProjectFactory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contact_client.R;

public class EndVideoCell extends ConstraintLayout {
    private TextView textViewId, textViewChoice;
    private ImageView imageViewRemove;

    public EndVideoCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.video_cell_end, this);
        textViewId = findViewById(R.id.textViewId);
        textViewChoice = findViewById(R.id.textViewChoice);
        imageViewRemove = findViewById(R.id.imageViewRemove);
    }

    public TextView getTextViewId() {
        return textViewId;
    }

    public TextView getTextViewChoice() {
        return textViewChoice;
    }

    public ImageView getImageViewRemove() {
        return imageViewRemove;
    }
}
