package com.permissionxs.backbootdemo

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.util.SortedMap
import java.util.TreeMap

object UsageDataUtils {

    private var packagename: String? = null

    fun getTopPackageName(context: Context): String {
        // if the sdk >= 21. It can only use getRunningAppProcesses to get task top package name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usage = context.getSystemService(AppCompatActivity.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
            if (stats != null) {
                val runningTask: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in stats) {
                    runningTask[usageStats.lastTimeUsed] = usageStats
                }
                if (!runningTask.isEmpty()) {
                    packagename = runningTask[runningTask.lastKey()]!!.packageName
                }
            }
        } else { // if sdk <= 20, can use getRunningTasks
            val am = context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
            val runningTasks = am.getRunningTasks(1)
            val runningTaskInfo = runningTasks[0]
            packagename = runningTaskInfo.topActivity!!.packageName

        }
        return packagename.toString()
    }

    fun isGoingToBackground(context:Context): Boolean {
        val topPackage = getTopPackageName(context)
        return topPackage != context.packageName
    }
}