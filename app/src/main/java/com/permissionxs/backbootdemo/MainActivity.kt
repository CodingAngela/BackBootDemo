package com.permissionxs.backbootdemo

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.SortedMap
import java.util.TreeMap


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1: Button = findViewById(R.id.button_1)
        button1.setOnClickListener {
//            NotificationUtils.sendNotificationFullScreen(this, "title", "content")
//            requestPostNotification(this)
            val boot_intent = Intent()
            boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$UsageAccessSettingsActivity")
            boot_intent.setData(Uri.fromParts("package", this.getPackageName(), null))
            boot_intent.setComponent(componentName)
            this.startActivity(boot_intent)
//            Toast.makeText(this, getTopPackageName(), Toast.LENGTH_SHORT).show()
//            finish()
        }
    }


    override fun onPause() {
        super.onPause()
        if (isGoingToBackground()){
//            Toast.makeText(this, getTopPackageName(), Toast.LENGTH_SHORT).show()
            val intent = Intent()
            val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$NotificationAppListActivity")
            intent.setData(Uri.fromParts("package", this.packageName, null))
            intent.setComponent(componentName)
            this.startActivity(intent)

        } else {
            Toast.makeText(this, getTopPackageName(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        val intent = Intent()
//        val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$NotificationAppListActivity")
//        intent.setData(Uri.fromParts("package", this.packageName, null))
//        intent.setComponent(componentName)
//        this.startActivity(intent)
//        NotificationUtils.sendNotificationFullScreen(this, "title", "content")
    }

    private fun isGoingToBackground(): Boolean {
//        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
//        val runningTasks = activityManager.getRunningTasks(1)
//        if (!runningTasks.isEmpty()) {
//            val topActivity = runningTasks[0].topActivity
//            if (topActivity!!.packageName != packageName) {
//                return true
//            }
//        }
//        return false
        val topPackage = getTopPackageName()
        return topPackage!! != packageName
    }

    private fun getTopPackageName(): String {
        var packagename: String? = null
        // if the sdk >= 21. It can only use getRunningAppProcesses to get task top package name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usage = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val stats =
                usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
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
            val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            //4.获取正在开启应用的任务栈
            val runningTasks = am.getRunningTasks(1)
            val runningTaskInfo = runningTasks[0]
            //5.获取栈顶的activity,然后在获取此activity所在应用的包名
            packagename = runningTaskInfo.topActivity!!.packageName

        }
        return packagename.toString()
    }

    fun requestPostNotification(context: Context?) {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                200
            )
        } else {
            Toast.makeText(context, "已经获取通知权限", Toast.LENGTH_SHORT).show()
        }
    }

}