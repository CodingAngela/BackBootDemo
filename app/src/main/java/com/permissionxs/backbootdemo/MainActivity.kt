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
import com.permissionxs.backbootdemo.UsageDataUtils.getTopPackageName
import com.permissionxs.backbootdemo.UsageDataUtils.isGoingToBackground
import java.util.SortedMap
import java.util.TreeMap


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1: Button = findViewById(R.id.button_1)
        button1.setOnClickListener {

            PermissionUtils.settingAppDetails(this@MainActivity)
        }
        val button2: Button = findViewById(R.id.button_2)
        button2.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }
    }


//    override fun onPause() {
//        super.onPause()
//        if (isGoingToBackground(this)){
//            backgroundAction()
//
//        } else {
//            Toast.makeText(this, getTopPackageName(this), Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun backgroundAction() {
        val intent = Intent()
        val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$NotificationAppListActivity")
        intent.setData(Uri.fromParts("package", this.packageName, null))
        intent.setComponent(componentName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }


}