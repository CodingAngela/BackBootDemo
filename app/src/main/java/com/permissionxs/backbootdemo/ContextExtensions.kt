package com.permissionxs.backbootdemo

import android.accessibilityservice.AccessibilityService
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.widget.Toast


/**
 * 判断目标辅助功能是否开启，未开启填转设置页面
 */
fun Context.isAccessibilitySettingsOn(clazz: Class<out AccessibilityService?>): Boolean {
    var accessibilityEnabled = false
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            applicationContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        ) == 1
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    val mStringColonSplitter = SimpleStringSplitter(':')
    if (accessibilityEnabled) {
        val settingValue: String? = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals("${packageName}/${clazz.canonicalName}", ignoreCase = true))
                    return true
            }
        }
    }
    return false
}

/**
 * 跳转其他app
 */
fun Context.startApp(packageName: String, activityName: String, errorTips: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            component = ComponentName(packageName, activityName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, errorTips, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.message?.let { message ->
            Log.d("exception", message)
        }
    }
}

fun Context.startApp(urlScheme: String, errorTips: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme)))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, errorTips, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.message?.let { message ->
            Log.d("exception", message)
        }
    }
}