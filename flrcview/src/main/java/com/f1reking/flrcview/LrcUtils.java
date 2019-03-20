package com.f1reking.flrcview;

import android.text.format.DateUtils;
import java.util.Locale;

/**
 * @author F1ReKing
 * @date 2019/3/20 13:48
 * @Description
 */
public class LrcUtils {

    public static String formatTime(long millis) {
        int m = (int) (millis / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((millis / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return mm + ":" + ss;
    }
}
