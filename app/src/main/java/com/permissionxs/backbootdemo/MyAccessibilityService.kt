package com.permissionxs.backbootdemo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.*
import android.app.Service
import android.content.Intent
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock.sleep
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
import android.view.accessibility.AccessibilityNodeInfo
import kotlin.math.log

class MyAccessibilityService : AccessibilityService() {
    val TAG = javaClass.simpleName

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(TAG, "onAccessibilityEvent: $event")

//        if (event?.eventType == TYPE_WINDOW_STATE_CHANGED) {
//            if (event.className == "com.example.forageservice.MainActivity") {
//                rootInActiveWindow.fullPrintNode("MainActivity")
//                event.source.fullPrintNode("MainActivity")
//                val nodeList = event.source?.findAccessibilityNodeInfosByText("ignore")
//                if (!nodeList.isNullOrEmpty()) {
//                    nodeList[0].click()
//                    performGlobalAction(GLOBAL_ACTION_BACK)
//                }
//            }
//        }

        if (event?.eventType == TYPE_WINDOW_STATE_CHANGED) {
            if (event.className == "com.example.permisssonm.MainActivity") {
//                rootInActiveWindow.fullPrintNode("MainActivity")
                rootInActiveWindow?.let { source ->
                    source.getNodeByText("照片和视频")?.scrollForward()
                    sleep(1000)
                    source.getNodeByText("无线和网络").click()
//                    source.getNodeByText("照片和视频")?.getParent()?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                }
//                val nodeList = event.source?.findAccessibilityNodeInfosByText("获取访问照片和视频权限")
//                if (!nodeList.isNullOrEmpty()) {

//                }
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }

    /**
     * 根据id查询并返回单个结点（第一个结点）
     */
    fun AccessibilityNodeInfo.getNodeById(id: String): AccessibilityNodeInfo? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByViewId(id).let { nodeList->
                if (!nodeList.isNullOrEmpty()) {
                    return nodeList[0]
                }
            }
            sleep(100)
            count++
        }
        return null
    }

    /**
     * 根据id查询并返回多个结点（list<node>）
     */
    fun AccessibilityNodeInfo.getNodesById(id: String): List<AccessibilityNodeInfo>? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByViewId(id).let { nodeList->
                if (!nodeList.isNullOrEmpty()) {
                    return nodeList
                }
            }
            sleep(100)
            count++
        }
        return null
    }

    /**
     * 根据text查询并返回单个结点（第一个结点）
     * @param allMatch 是否全匹配，默认false
     */
    fun AccessibilityNodeInfo.getNodeByText(text: String, allMatch: Boolean = false): AccessibilityNodeInfo? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByText(text).let { nodeList->
                if (!nodeList.isNullOrEmpty()) {
                    if (allMatch){
                        nodeList.forEach { node -> if (node.text() == text) return node }
                    } else {
                        return nodeList[0]
                    }
                }
            }
            sleep(100)
            count++
        }
        return null
    }


    /**
     * 根据text查询并返回多个结点（list<node>）
     * @param allMatch 是否全匹配，默认false
     */
    fun AccessibilityNodeInfo.getNodesByText(text: String, allMatch: Boolean = false): List<AccessibilityNodeInfo>? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByText(text).let { nodeList->
                if (!nodeList.isNullOrEmpty()) {
                    return if (allMatch){
                        val tempList = arrayListOf<AccessibilityNodeInfo>()
                        nodeList.forEach { node -> if (node.text() == text) tempList.add(node) }
                        if (tempList.isEmpty()) null else tempList
                    } else {
                        nodeList
                    }
                }
            }
            sleep(100)
            count++
        }
        return null
    }

    /**
     * 获得结点文本内容
     */
    fun AccessibilityNodeInfo?.text(): String {
        return this?.text?.toString() ?: ""
    }

    fun AccessibilityNodeInfo?.fullPrintNode(
        tag: String,
        spaceCount: Int = 0
    ) {
        if (this == null) return
        val spaceSb = StringBuilder().apply { repeat(spaceCount) { append("  ") } }
        Log.d(TAG, "$tag: $spaceSb$text | $viewIdResourceName | $className | Clickable: $isClickable")
        if (childCount == 0) return
        for (i in 0 until childCount)
            getChild(i).fullPrintNode(tag, spaceCount + 1)
    }



    fun AccessibilityNodeInfo?.click() {
        if (this == null) return
        if (this.isClickable) {
            this.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return
        } else {
            this.parent.click()
        }
    }

    fun AccessibilityNodeInfo?.longClick() {
        if (this == null) return
        if (this.isClickable) {
            this.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
            return
        } else {
            this.parent.longClick()
        }
    }

    fun AccessibilityNodeInfo.scrollForward() =
        performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)

    fun AccessibilityNodeInfo.scrollBackward() =
        performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)

    fun AccessibilityNodeInfo.input(content: String) = performAction(
        AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content)
        }
    )

    /**
     * 手势模拟点击
     * @param node 需要点击的结点
     */
    fun AccessibilityService.gestureClick(node: AccessibilityNodeInfo?) {
        if (node == null) return
        val tempRect = Rect()
        node.getBoundsInScreen(tempRect)
        val x = ((tempRect.left + tempRect.right) / 2).toFloat()
        val y = ((tempRect.top + tempRect.bottom) / 2).toFloat()
        dispatchGesture(
            Builder().apply {
                addStroke(StrokeDescription(Path().apply{ moveTo(x, y) }, 0L,200L))
            }.build(),
            object : AccessibilityService.GestureResultCallback() {
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    Log.d(TAG, "手势点击完成[$x - $y]")
                }
            },
            null
        )
    }
}