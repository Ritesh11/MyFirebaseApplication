package com.sample.myfirebaseapplication


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationID = 0
        if (intent != null) {
            notificationID = intent.getIntExtra("NOTIFICATION_ID", 0)
            if (notificationID != 0) {
                val notificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationId = intent!!.getIntExtra("NOTIFICATION_ID", 0)
                notificationManager.cancel(notificationId)

            }
        }
        context?.let {
            when (intent?.action) {


                ACTION_YES -> {
                    startMainActivity(context, "API", notificationID)
                }
                ACTION_NO -> showToast(context, "No clicked")
                ACTION_OPEN -> startMainActivity(context, "OPEN", notificationID)
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startMainActivity(context: Context, data: String?, notificationId: Int) {
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mainActivityIntent.action = data
        mainActivityIntent.putExtra("NOTIFICATION_ID", notificationId)
        context.startActivity(mainActivityIntent)
    }

    companion object {
        const val ACTION_YES = "action_yes"
        const val ACTION_NO = "action_no"
        const val ACTION_OPEN = "action_open"

        fun createPendingIntent(context: Context, intent: Intent): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
    }
}