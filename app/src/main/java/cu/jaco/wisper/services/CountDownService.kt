package cu.jaco.wisper.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import cu.jaco.wisper.R
import cu.jaco.wisper.utils.NotificationUtils
import cu.jaco.wisper.utils.wipeData

class CountDownService : Service() {

    private var timer: CountDownTimer? = null

    companion object {

        const val TICK_ACTION = "cu.jaco.wisper.services.ON_TICK"
        const val ARG_LEFT_TIME = "ARG_LEFT_TIME"

        private const val ARG_CANCEL = "ARG_CANCEL"

        fun startIntent(context: Context): Intent {
            val intent = Intent(context, CountDownService::class.java)
            intent.putExtra(ARG_CANCEL, false)
            return intent
        }

        fun cancelIntent(context: Context): Intent {
            val intent = Intent(context, CountDownService::class.java)
            intent.putExtra(ARG_CANCEL, true)
            return intent
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.getBooleanExtra(ARG_CANCEL, false) != true)
            startCommand()
        else cancelCommand()
    }

    fun startTimer() {

        if (timer != null)
            return

        timer = object : CountDownTimer(30000, 1000) {

            override fun onTick(leftTime: Long) {
                sendTickBroadcast(leftTime)
            }

            override fun onFinish() {
                wipeData()
                timer = null
                stopSelf()
            }

        }.start()

    }

    private fun sendTickBroadcast(leftTime: Long) {
        sendBroadcast(Intent(TICK_ACTION).apply {
            putExtra(ARG_LEFT_TIME, leftTime)
        })
    }

    private fun cancelCommand(): Int {
        timer?.cancel()
        timer = null
        sendTickBroadcast(-1)
        stopSelf()
        return START_NOT_STICKY
    }

    private fun startCommand(): Int {

        val mBuilder =
            NotificationCompat.Builder(this, NotificationUtils.NOTIFICATION_CHANEL_COUNTDOWN_ID)
                .setSmallIcon(R.drawable.ic_time_bomb)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(getString(R.string.notification_timer_title))
                .setContentText(getString(R.string.notification_timer_message))
//                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setAutoCancel(false)

        startForeground(1, mBuilder.build())

        startTimer()

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}