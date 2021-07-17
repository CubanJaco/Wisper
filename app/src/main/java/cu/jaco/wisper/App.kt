package cu.jaco.wisper

import android.annotation.TargetApi
import android.app.Application
import cu.jaco.wisper.repositories.preferences.AppPreferences
import cu.jaco.wisper.utils.NotificationUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            createNotificationsChannels()
    }

    @TargetApi(26)
    private fun createNotificationsChannels() {
        NotificationUtils.createChannel(this)
    }

}