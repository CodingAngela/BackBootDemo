package com.permissionxs.backbootdemo

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.Service
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.view.WindowManager
import android.widget.Toast
import kotlin.concurrent.thread

class MyService : Service() {

    var number: Int ?= 0
    lateinit var manager:AlarmManager
    lateinit var pi: PendingIntent


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (number != 0) {
           thread {
               val msg = Message()
               msg.what = 1
               mHandler.sendMessage(msg)
           }
        }
        manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = 10 * 1000
        val triggerTime: Long = SystemClock.elapsedRealtime() + (time)
        val intent = Intent(this@MyService, AlarmReceiver::class.java)
        pi = PendingIntent.getBroadcast(this@MyService, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi)
        number = number!! + 1

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.cancel(pi)
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(this@MyService, R.style.AlertDialogCustom)
                    builder.setTitle("安卓气泡弹窗")
                    builder.setMessage("这是由service拉起的气泡弹窗")
                    builder.setCancelable(false)
                    builder.setPositiveButton("确定", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                            Toast.makeText(this@MyService, "OK Clicked", Toast.LENGTH_SHORT).show()
                        }
                    })
                    builder.setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            Toast.makeText(this@MyService, "Cancel Clicked", Toast.LENGTH_SHORT).show()
                        }
                    })
                    val dialog: AlertDialog = builder.create()
                    dialog.getWindow()?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
//                    dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.shape_bg)
                    dialog.show()
                }
            }
        }
    }


}