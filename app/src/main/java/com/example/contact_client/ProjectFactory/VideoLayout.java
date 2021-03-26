package com.example.contact_client.ProjectFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contact_client.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class VideoLayout extends LinearLayout {
    private boolean isNormalCell;
    private ConstraintLayout videoCell;

    public int ownId;
    public int preId;

    @SuppressLint("ResourceType")
    public VideoLayout(Context context, boolean isNormalCell, int ownId, int preId) {
        super(context);

        this.isNormalCell = isNormalCell;
        this.ownId = ownId;
        this.preId = preId;

        XmlPullParser parser;
        if (isNormalCell == FactoryConstant.NormalCell) {
            parser = context.getResources().getXml(R.layout.video_cell_normal);
        }else {
            parser = context.getResources().getXml(R.layout.video_cell_end);
        }
        AttributeSet attribute = Xml.asAttributeSet(parser);
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT){

            }
            if (type != XmlPullParser.START_TAG){
                Log.e("me", "the xml file is error!\n");
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (isNormalCell == FactoryConstant.NormalCell) {
            this.videoCell = new NormalVideoCell(context, attribute);
        }else {
            this.videoCell = new EndVideoCell(context, attribute);
        }
        this.addView(videoCell);
    }

    public ConstraintLayout getVideoCell() {
        return videoCell;
    }

    public void setAction(){

    }
}
