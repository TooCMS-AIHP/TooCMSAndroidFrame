package cn.zero.android.common.view.banner.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.zero.android.common.util.ScreenUtils;

/**
 * adapter中调用onCreateViewHolder, onBindViewHolder
 */
public class CBPageAdapterHelper {
    public static int sPagePadding = 0;
    public static int sShowLeftCardWidth = 0;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = parent.getWidth() - ScreenUtils.dpToPxInt(2 * (sPagePadding + sShowLeftCardWidth));
        itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
        int padding = ScreenUtils.dpToPxInt(sPagePadding);
        itemView.setPadding(padding, 0, padding, 0);
        int leftMarin = position == 0 ? padding + ScreenUtils.dpToPxInt(sShowLeftCardWidth) : 0;
        int rightMarin = position == itemCount - 1 ? padding + ScreenUtils.dpToPxInt(sShowLeftCardWidth) : 0;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    public void setPagePadding(int pagePadding) {
        sPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        sShowLeftCardWidth = showLeftCardWidth;
    }
}