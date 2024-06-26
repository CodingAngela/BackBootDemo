package com.permissionxs.backbootdemo

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
import android.view.accessibility.AccessibilityNodeInfo
import kotlin.math.log

class MyAccessibilityService : AccessibilityService() {
    val TAG = javaClass.simpleName

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType == TYPE_WINDOW_STATE_CHANGED) {
            if (event.className == "com.example.permisssonm.MainActivity") {
                val nodeList = event.source?.findAccessibilityNodeInfosByText("获取访问照片和视频权限")
                if (!nodeList.isNullOrEmpty()) {
//                    Log.d(TAG, "onAccessibilityEvent: $event")
                    nodeList[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.d(TAG, nodeList[0].toString())
                }
//                Log.d(TAG, nodeList[0].toString())
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }
}