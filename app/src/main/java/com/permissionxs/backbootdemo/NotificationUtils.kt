package com.permissionxs.backbootdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationUtils {
    private const val ID = "channel_1"
    private const val NAME = "notification"

    private var manager: NotificationManager? = null



    private fun getNotificationManagerManager(context: Context): NotificationManager? {
        if (manager == null){
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        }
        return manager
    }

    fun sendNotificationFullScreen(context: Context, title: String?, content: String?) {
        if (Build.VERSION.SDK_INT >= 26) {
            clearAllNotification(context)
            val channel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.setSound(null, null)
            getNotificationManagerManager(context)?.createNotificationChannel(channel)
            val notification = getChannelNotificationQ(context, title, content)
            getNotificationManagerManager(context)?.notify(1, notification)
        }
    }
    private fun clearAllNotification(context: Context) {
        getNotificationManagerManager(context)?.cancelAll()
    }

    private fun getChannelNotificationQ(context: Context, title: String?, content: String?): Notification {
        val intent =Intent()
        val componentName = ComponentName.unflattenFromString("com.android.settings/.Settings\$NotificationAppListActivity")
        intent.setData(Uri.fromParts("package", context.packageName, null))
        intent.setComponent(componentName)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(context, ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_CALL)
            .setOngoing(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)
        return notificationBuilder.build()
    }


}