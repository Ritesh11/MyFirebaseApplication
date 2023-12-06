package com.sample.myfirebaseapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sample.myfirebaseapplication.NotificationActionReceiver.Companion.ACTION_YES


const val CHANNEL_ID = "Notification Channel"
const val CHANNEL_NAME = "com.sample.myfirebaseapplication"
const val NOTIFICATION_ID = 1

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Generate notification
    // Attach notification to custom layout
    // Show notification
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("remoteMessage",remoteMessage.toString())
        if (remoteMessage.data.size > 0) {
            val data = remoteMessage.data
            val title = data["title"]
            val body = data["body"]
            val customLayoutName = data["custom_layout"]

            generateNotification(title, body, customLayoutName)

            Log.e("Receiving Data",data.get("title") + "    " + data.get("description"))
        } else if (remoteMessage.notification != null) {
            val title =  remoteMessage.notification!!.title!!
            val body = remoteMessage.notification!!.body!!
            generateNotification(title, body,"")

            Log.e("Receiving Data",remoteMessage.notification!!.title!! + "    " + remoteMessage.notification!!.body!!)
        }
    }

    fun generateNotification(title: String?, desc: String?,customLayout: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("NOTIFICATION_ID",NOTIFICATION_ID)
        val pendingActivity = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_MUTABLE)

        // Create an intent to handle button clicks

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        val remoteCustomView = getRemoteView(title!!,desc!!)

        // Build the notification
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingActivity)
                .setCustomBigContentView(remoteCustomView)
//        builder = builder.setContent(getRemoteView(title!!,desc!!))


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, desc: String): RemoteViews {

        val yesIntent = Intent(applicationContext, NotificationActionReceiver::class.java).apply {
            action = ACTION_YES
        }

        yesIntent.putExtra("NOTIFICATION_ID",NOTIFICATION_ID)

        val yesPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, yesIntent,  PendingIntent.FLAG_CANCEL_CURRENT)


        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notificaion)
        remoteViews.setTextViewText(R.id.title_txt, title)
        remoteViews.setTextViewText(R.id.desc_txt, desc)
        remoteViews.setImageViewResource(R.id.notification_img, R.drawable.ic_launcher_foreground)
        remoteViews.setOnClickPendingIntent(R.id.accept, yesPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.regect, yesPendingIntent)

        return remoteViews
    }

    override fun onNewToken(token: String) {
        Log.e("FCM Token", token)
    }
}