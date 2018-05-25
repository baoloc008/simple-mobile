package com.simple.simplejobfinder.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.activities.MainActivity

class JobFinderFirebaseMessagingService : FirebaseMessagingService()
{

    private val TAG = JobFinderFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage?)
    {
        Log.d(TAG, "Have a message.")
        remoteMessage?.let {
            val message = remoteMessage.notification?.body
            val title = remoteMessage.notification?.title

            Log.d(TAG, "$title - $message")

            message?.let {
                pushNotification(title ?: "", message, false)
            }
        }
    }

    private fun pushNotification(title: String, content: String, isOngoing: Boolean)
    {
        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(isOngoing)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setAutoCancel(true)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)
        builder.setContentIntent(contentIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}
