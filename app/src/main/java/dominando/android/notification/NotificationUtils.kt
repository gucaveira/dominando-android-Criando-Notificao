package dominando.android.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat

@SuppressLint("MissingPermission")
object NotificationUtils {
    private const val CHANNEL_ID = "default"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = context.getString(R.string.notif_channel_name)
        val channelDescription = context.getString(R.string.notif_channel_description)

        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelDescription
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun getContentIntent(context: Context): PendingIntent? {
        val detailsIntent = Intent(context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_MESSAGE, "Via notificação")
        }
        // usar isso é bem util, focar em memorizar
        return TaskStackBuilder
            .create(context)
            .addNextIntentWithParentStack(detailsIntent)
            .getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
    }

    fun notificationSimple(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.purple_200))
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(1, notificationBuilder.build())
    }

    fun notificationWithTapAction(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.purple_700))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(getContentIntent(context))
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(2, notificationBuilder.build())
    }

    fun notificationBigText(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(context.getString(R.string.notif_big_message))

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.purple_200))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(getContentIntent(context))
            .setAutoCancel(true)
            .setStyle(bigTextStyle)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(3, notificationBuilder.build())
    }

    fun notificationWithButtonAction(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val actionIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra(NotificationActionReceiver.EXTRA_MESSAGE, "Ação da notificação")
        }

        val pendingIntent = PendingIntent
            .getBroadcast(
                context,
                0,
                actionIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.purple_200))
            .setDefaults(Notification.DEFAULT_ALL)
            .addAction(0, context.getString(R.string.notif_button_action), pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(4, notificationBuilder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun notificationAutoReply(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val notificationId = 5
        val intent = Intent(context, ReplyReceiver::class.java).apply {
            putExtra(ReplyReceiver.EXTRA_NOTIFICATION_ID, notificationId)
        }

        val replyPendingIntent = PendingIntent
            .getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        // pacote androidx.core.app
        val remoteInput = RemoteInput
            .Builder(ReplyReceiver.EXTRA_TEXT_REPLY)
            .setLabel(context.getString(R.string.notif_reply_hint))
            .build()

        val action = NotificationCompat.Action
            .Builder(
                R.drawable.ic_send,
                context.getString(R.string.notif_reply_label),
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.purple_200))
            .setDefaults(Notification.DEFAULT_ALL)
            .addAction(action)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun notificationReplied(context: Context, notificationId: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val timeout = 2000L

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_reply_replied))
            .setColor(ContextCompat.getColor(context, R.color.purple_200))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setDefaults(0)
            .setTimeoutAfter(timeout)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Handler(Looper.getMainLooper())
                .postDelayed({ notificationManager.cancel(notificationId) }, timeout)
        }
    }

    fun notificationInbox(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val number = 5

        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle(context.getString(R.string.notif_big_inbox_title))

        for (i in 1..number) {
            inboxStyle.addLine(context.getString(R.string.notif_big_inbox_message, i))
        }

        inboxStyle.setSummaryText(context.getString(R.string.notif_big_inbox_summary))

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setColor(ActivityCompat.getColor(context, R.color.purple_200))
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setDefaults(Notification.DEFAULT_ALL).setNumber(number)
            .setStyle(inboxStyle)

        val nm = NotificationManagerCompat.from(context)
        nm.notify(8, notificationBuilder.build())
    }

    fun notificationHeadsUp(mainActivity: MainActivity) {
        TODO("Not yet implemented")
    }
}