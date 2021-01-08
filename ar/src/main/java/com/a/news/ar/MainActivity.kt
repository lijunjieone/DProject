package com.a.news.ar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.news.ar.profile.monitor.time.TimeMonitorConfig
import com.a.news.ar.profile.monitor.time.TimeMonitorManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).recodingTimeTag("AppStartActivity_create")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).recodingTimeTag("AppStartActivity_createOver")


    }

    override fun onStart() {
        super.onStart()
        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).end("AppStartActivity_start", false)
    }

}