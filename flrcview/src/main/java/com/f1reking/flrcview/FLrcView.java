package com.f1reking.flrcview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author F1ReKing
 * @date 2019/3/20 13:48
 * @Description FLrcView
 */
public class FLrcView extends View {

    public FLrcView(Context context) {
        super(context);
    }

    public FLrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FLrcView(Context context, AttributeSet attrs,
        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 加载歌词文件
     */
    public void loadLrc() {

    }

    public void updateTime(final long time) {
        runOnUi(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void runOnUi(Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            post(r);
        }
    }
}
