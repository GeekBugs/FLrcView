package com.f1reking.flrcview;

import android.text.TextUtils;
import android.text.format.DateUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author F1ReKing
 * @date 2019/3/20 13:48
 * @Description
 */
public class LrcEntity implements Comparable<LrcEntity> {

    private static Pattern LINE_PATTERN =
        Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{1,3}])+)([\\s\\S]*)");
    private static Pattern TIME_PATTERN = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d){1,3}]");

    private long time;
    private String text;

    public LrcEntity(long time, String text) {
        this.time = time;
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(LrcEntity lrcEntity) {
        if (lrcEntity == null) {
            return -1;
        }
        return (int) (time - lrcEntity.getTime());
    }

    private static List<LrcEntity> parseLrc(File lrcFile) {
        if (null == lrcFile || !lrcFile.exists()) {
            return null;
        }
        List<LrcEntity> list = new ArrayList<>();
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                List<LrcEntity> entityList = parseLine(line);
                if (entityList != null && !entityList.isEmpty()) {
                    list.addAll(entityList);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list);
        return list;
    }

    private static List<LrcEntity> parseLrc(String lrcText) {
        if (TextUtils.isEmpty(lrcText)) {
            return null;
        }
        List<LrcEntity> list = new ArrayList<>();
        String[] textArray = lrcText.split("\\n");
        for (String line : textArray) {
            List<LrcEntity> entityList = parseLine(line);
            if (entityList != null && !entityList.isEmpty()) {
                list.addAll(entityList);
            }
        }
        Collections.sort(list);
        return list;
    }

    private static List<LrcEntity> parseLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }
        line = line.trim();
        Matcher lineMatcher = LINE_PATTERN.matcher(line);
        if (!lineMatcher.matches()) {
            return null;
        }
        String times = lineMatcher.group(1);
        String text = lineMatcher.group(3);
        List<LrcEntity> list = new ArrayList<>();

        Matcher timeMatcher = TIME_PATTERN.matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            long mil = Long.parseLong(timeMatcher.group(3));
            long time =
                min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + (mil >= 100L
                    ? mil : mil * 10);
            list.add(new LrcEntity(time, text));
        }
        return list;
    }
}
