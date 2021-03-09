package com.example.contact_client;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ButtonView extends RelativeLayout {
    private static String TAG = ButtonView.class.getSimpleName();

    private boolean isShowBottomLine = true;
    private boolean isShowLeftIcon = true;
    private boolean isShowRightArrow = true;
    private ImageView leftIcon; // 左侧图标
    private TextView leftTitle; // 左侧标题
    private ImageView rightArrow;   // 右侧箭头
    private ImageView bottomLine;   // 下划线
    private RelativeLayout rootView;  // 整体的view

    public ButtonView(@NonNull Context context) {
        this(context, null);
    }

    public ButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 添加布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_user_button, null);
        addView(view);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonView);

        // 绑定控件
        leftIcon = findViewById(R.id.left_icon);
        leftTitle = findViewById(R.id.left_title);
        rightArrow = findViewById(R.id.right_arrow);
        bottomLine = findViewById(R.id.bottom_line);
        rootView = findViewById(R.id.root_item);

        // 设置控件属性
        isShowBottomLine = ta.getBoolean(R.styleable.ButtonView_show_bottom_line, true);
        isShowLeftIcon = ta.getBoolean(R.styleable.ButtonView_show_left_icon, true);
        isShowRightArrow = ta.getBoolean(R.styleable.ButtonView_show_right_arrow, true);

        leftIcon.setBackground(ta.getDrawable(R.styleable.ButtonView_left_icon));
        leftIcon.setVisibility(isShowLeftIcon ? View.VISIBLE : View.INVISIBLE);

        leftTitle.setText(ta.getString(R.styleable.ButtonView_left_title));

        rightArrow.setVisibility(isShowRightArrow ? View.VISIBLE : View.INVISIBLE);
        bottomLine.setVisibility(isShowBottomLine ? View.VISIBLE : View.INVISIBLE);

        // 设计点击事件
        // 给整个item设置点击事件
        rootView.setOnClickListener((v) -> {
            listener.itemClick();
        });
        // 给右侧箭头设置点击事件
        rightArrow.setOnClickListener((v) -> {
            listener.itemClick();
        });

        ta.recycle();
    }

    // 设置左侧图标
    public void setLeftIcon(int value) {
        Drawable drawable = getResources().getDrawable(value);
        leftIcon.setBackground(drawable);
    }

    // 设置左侧标题文字
    public void setLeftTitle(String value) {
        leftTitle.setText(value);
    }

    // 设置右侧箭头
    public void setShowRightArrow(boolean value) {
        rightArrow.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
    }

    // 设置是否显示下划线
    public void setShowBottomLine(boolean value) {
        bottomLine.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
    }

    // 接口
    public interface itemClickListener {
        void itemClick();
    }

    private itemClickListener listener;

    public void setItemClickListener(itemClickListener listener) {
        this.listener = listener;
    }
}
