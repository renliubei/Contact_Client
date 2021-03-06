package com.example.contact_client.project_manager;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    // 每一个页面默认页边距
    int mPageMargin = 10;
    // 中间页面左右两边的页面可见部分宽度
    int mLeftPageVisibleWidth = 30;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        onSetHorizontalParams(parent,view,position,itemCount);
    }

    private void onSetHorizontalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        //注意 我们xml中定义了padding，所以才没有贴顶
        int itemNewWidth = parent.getWidth() - dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
        int itemNewHeight = parent.getHeight();
        //mPageMargin就是页之间的边距 默认是0 由于有padding 所以没有贴在一起 我觉得设计的非常不合理 但是无伤大雅

        int leftMargin = position == 0 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);

        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth, int itemHeight) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;
        }

        // 这里的if考虑到的是缩放动画情况
        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
