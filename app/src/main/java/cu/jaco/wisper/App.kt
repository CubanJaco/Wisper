package cu.jaco.wisper

import android.app.Application
import cu.jaco.wisper.repositories.preferences.AppPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}