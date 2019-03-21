package com.f1reking.flrcview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author F1ReKing
 * @date 2019/3/20 13:48
 * @Description FLrcView
 */
public class FLrcView extends View {

    private static final String TAG = FLrcView.class.getSimpleName();

    private int height = 0;
    private int width = 0;
    private Paint mPaint;
    private List<LrcEntity> mLrcEntities = new ArrayList<>();

    public FLrcView(Context context) {
        this(context, null);
    }

    public FLrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FLrcView(Context context, AttributeSet attrs,
        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(18);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (mLrcEntities.isEmpty()) {
            canvas.drawText("暂无歌词", width / 2, height / 2, mPaint);
            return;
        }
    }

    /**
     * 加载歌词文件
     */
    public void loadLrc(final File lrcFile) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<File, Integer, List<LrcEntity>>() {
                    @Override
                    protected List<LrcEntity> doInBackground(File... params) {
                        return LrcEntity.parseLrc(params[0]);
                    }

                    @Override
                    protected void onPostExecute(List<LrcEntity> lrcEntities) {
                        onLoadLrc(lrcEntities);
                    }
                }.execute(lrcFile);
            }
        });
    }

    /**
     * 加载歌词文本
     */
    public void loadLrc(final String lrcText) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<String, Integer, List<LrcEntity>>() {
                    @Override
                    protected List<LrcEntity> doInBackground(String... params) {
                        return LrcEntity.parseLrc(params[0]);
                    }

                    @Override
                    protected void onPostExecute(List<LrcEntity> entityList) {
                        onLoadLrc(entityList);
                    }
                }.execute(lrcText);
            }
        });
    }

    /**
     * 加载在线歌词
     */
    public void loadLrcUrl(final String lrcUrl) {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                String lrcText = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(10000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        int size = conn.getContentLength();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        lrcText = new String(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return lrcText;
            }

            @Override
            protected void onPostExecute(String s) {
                loadLrc(s);
            }
        }.execute(lrcUrl);
    }

    /**
     * 歌词是否有效
     */
    public boolean hasLrc() {
        return !mLrcEntities.isEmpty();
    }

    private void onLoadLrc(List<LrcEntity> entityList) {
        if (entityList != null && !entityList.isEmpty()) {
            mLrcEntities.addAll(entityList);
        }
    }

    /**
     * 刷新时间
     */
    public void updateTime(final long time) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                if (!hasLrc()) {
                    return;
                }
                invalidate();
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
