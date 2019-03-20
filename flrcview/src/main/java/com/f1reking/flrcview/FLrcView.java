package com.f1reking.flrcview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author F1ReKing
 * @date 2019/3/20 13:48
 * @Description FLrcView
 */
public class FLrcView extends View {

    private List<LrcEntity> mLrcEntities = new ArrayList<>();

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
    public void loadLrc(final File lrcFile) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<File, Integer, List<LrcEntity>>() {
                    @Override
                    protected List<LrcEntity> doInBackground(File... files) {
                        return LrcEntity.parseLrc(files[0]);
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
                    protected List<LrcEntity> doInBackground(String... strings) {
                        return LrcEntity.parseLrc(strings[0]);
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
    public void loadLrcUrl(final String url) {
        new AsyncTask<String,Integer,String>(){

            @Override
            protected String doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
            }
        }.execute(url);
    }

    private void onLoadLrc(List<LrcEntity> entityList) {
        if (entityList != null && !entityList.isEmpty()) {
            mLrcEntities.addAll(entityList);
        }
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
