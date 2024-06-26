package com.permissionxs.backbootdemo

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

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

    fun settingUsageAccess(context: Context) {
        val boot_intent = Intent()
        boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$UsageAccessSettingsActivity")
        boot_intent.setData(Uri.fromParts("package", context.getPackageName(), null))
        boot_intent.setComponent(componentName)
        context.startActivity(boot_intent)
    }

    fun settingAppDrawOverlay(context: Context) {
        if (Build.BRAND == "OnePlus") {
            val boot_intent = Intent()
            boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            boot_intent.setAction("oplus.intent.action.settings.MANAGE_APP_OVERLAY_PERMISSION")
            context.startActivity(boot_intent)
        } else if (Build.BRAND == "google") {
            val boot_intent = Intent()
            boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$AppDrawOverlaySettingsActivity")
            boot_intent.setData(Uri.fromParts("package", context.packageName, null))
            boot_intent.setComponent(componentName)
            context.startActivity(boot_intent)
        }
    }

    fun requestIgnoreBatteryOptimization(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val hasIgnored = powerManager.isIgnoringBatteryOptimizations(context.packageName)

        if (!hasIgnored) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(Uri.parse("package:" + context.packageName))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        } else {
            Toast.makeText(context, "已经忽略电池优化", Toast.LENGTH_SHORT).show()
        }
    }

    fun settingAppDetails(context: Context) {
        if (Build.BRAND == "OnePlus") {
            val boot_intent = Intent()
            boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            boot_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
            boot_intent.setData(Uri.fromParts("package", context.packageName, null))
            context.startActivity(boot_intent)
        } else if (Build.BRAND == "google") {
            val boot_intent = Intent()
            boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val componentName = ComponentName.unflattenFromString("com.android.settings/.applications.InstalledAppDetails")
            boot_intent.setData(Uri.fromParts("package", context.packageName, null))
            boot_intent.setComponent(componentName)
            context.startActivity(boot_intent)
        } else {
            Toast.makeText(context, "待实现", Toast.LENGTH_SHORT).show()
        }
    }
}