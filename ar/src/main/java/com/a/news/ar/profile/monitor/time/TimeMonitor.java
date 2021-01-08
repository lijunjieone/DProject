package com.a.news.ar.profile.monitor.time;


import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by yuchengluo on 2016/3/25.
 */
public class TimeMonitor {
    private final String TAG = "TimeMonitor";
    private int monitorId = -1;
    private LinkedHashMap<String, Long> mTimeTag = new LinkedHashMap<String, Long>();
    private long mStartTime = 0;

    public TimeMonitor(int id) {
        Log.d(TAG, "init TimeMonitor id:" + id);
        monitorId = id;
        startMoniter();
    }

    public int getMonitorId() {
        return monitorId;
    }

    public void startMoniter() {
        if (mTimeTag.size() > 0) {
            mTimeTag.clear();
        }
        mStartTime = System.currentTimeMillis();
    }

    public void recodingTimeTag(String tag) {
        if (mTimeTag.get(tag) != null) {
            mTimeTag.remove(tag);
        }
        long time = System.currentTimeMillis() - mStartTime;
        Log.d(TAG, tag + ":" + time + "ms");
        mTimeTag.put(tag, time);
    }

    public void end(String tag, boolean writeLog) {
        recodingTimeTag(tag);
        end(writeLog);
    }

    public void end(boolean writeLog) {
        if (writeLog) {
            //TODO write local
        }
        testShowData();
    }

    public void testShowData() {
        if (mTimeTag.size() <= 0) {
            Log.e(TAG, "mTimeTag is empty!");
            return;
        }
        Iterator iterator = mTimeTag.keySet().iterator();
        while (iterator != null && iterator.hasNext()) {
            String tag = (String) iterator.next();
            Log.d(TAG, tag + ":" + mTimeTag.get(tag));
        }
    }

    public HashMap<String, Long> getTimeTags() {
        return mTimeTag;
    }
}
