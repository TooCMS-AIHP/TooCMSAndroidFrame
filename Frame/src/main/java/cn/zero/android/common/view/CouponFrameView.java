package cn.zero.android.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.zhy.autolayout.AutoLinearLayout;


/**
 * 优惠券效果控件
 *
 * @author Zero
 * @date 2016/7/27 10:56
 */
public class CouponFrameView extends AutoLinearLayout {

    private int circleNum; // 圆数量
    private float gap = 8; // 圆间距
    private float radius = 10; // 半径
    private float remain; // 剩余部分

    private Paint mPaint;

    public CouponFrameView(Context context) {
        super(context);
    }

    public CouponFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public CouponFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (remain == 0) {
            // 计算不整除的剩余部分
            remain = (int) (w - gap) % (2 * radius + gap);
        }
        circleNum = (int) ((w - gap) / (2 * radius + gap));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < circleNum; i++) {
            float x = gap + radius + remain / 2 + ((gap + radius * 2) * i);
            canvas.drawCircle(x, 0, radius, mPaint);
            canvas.drawCircle(x, getHeight(), radius, mPaint);
        }
    }
}
