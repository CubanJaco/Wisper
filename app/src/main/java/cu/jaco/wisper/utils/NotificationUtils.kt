package cu.jaco.wisper.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.annotation.RequiresApi
import cu.jaco.wisper.R

object NotificationUtils {

    const val NOTIFICATION_CHANEL_COUNTDOWN_ID = "NOTIFICATION_CHANEL_COUNTDOWN_ID"

    @TargetApi(26)
    @RequiresApi(26)
    @Synchronized
    fun createChannel(
        context: Context,
        name: String = context.getString(R.string.notification_countdown_chanel_name),
        channelId: String = NOTIFICATION_CHANEL_COUNTDOWN_ID,
        importance: Int = NotificationManager.IMPORTANCE_HIGH,
        sound: Boolean = false
    ): String {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mChannel = NotificationChannel(channelId, name, importance)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val att = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        if (importance == NotificationManager.IMPORTANCE_LOW)
            mChannel.setShowBadge(false)
        if (sound)
            mChannel.setSound(notificationSound, att)

        mNotificationManager.createNotificationChannel(mChannel)

        return channelId
    }

}