package com.a.news.ar;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.a.news.ar.profile.CrashHandler;
import com.a.news.ar.profile.monitor.time.TimeMonitorConfig;
import com.a.news.ar.profile.monitor.time.TimeMonitorManager;

/**
 * Created by yuchengluo on 2015/6/25.
 */
public class GmfApplication extends Application {

    private static Context mContext = null;

    public static Context getmContext() {
        return mContext;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = this;
        TimeMonitorManager.getInstance().resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START);
        //初始化图片引擎


    }

    @Override
    public void onCreate() {
        super.onCreate();

        InitModule();
        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).recodingTimeTag("ApplicationCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void InitModule() {
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(this);
    }
}
