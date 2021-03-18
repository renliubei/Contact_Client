package com.example.contact_client.ProjectFactory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contact_client.R;

public class NormalVideoCell extends ConstraintLayout {
    private TextView textViewId, textViewChoice;
    private ImageView imageViewAdd, imageViewMenu;

    public NormalVideoCell(Context context, @Nullable AttributeSet attr){
        super(context, attr);
        LayoutInflater.from(context).inflate(R.layout.video_cell_normal, this);
        textViewId = findViewById(R.id.textViewId);
        textViewChoice = findViewById(R.id.textViewChoice);
        imageViewAdd = findViewById(R.id.imageViewAdd);
        imageViewMenu = findViewById(R.id.imageViewMenu);
    }

    public TextView getTextViewId() {
        return textViewId;
    }

    public TextView getTextViewChoice() {
        return textViewChoice;
    }

    public ImageView getImageViewAdd() {
        return imageViewAdd;
    }

    public ImageView getImageViewMenu() {
        return imageViewMenu;
    }
}
