package cu.jaco.wisper.utils

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast
import cu.jaco.wisper.BuildConfig
import cu.jaco.wisper.R
import cu.jaco.wisper.repositories.preferences.AppPreferences
import cu.jaco.wisper.services.WipeDataReceiver

fun Context.wipeData() {

    val mDPM = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val mDeviceAdmin = ComponentName(this, WipeDataReceiver::class.java)

    val flags = if (AppPreferences.clearExternalStorage)
        DevicePolicyManager.WIPE_EXTERNAL_STORAGE
    else 0

    if (mDPM.isAdminActive(mDeviceAdmin)) {
        if (BuildConfig.DEBUG)
            Toast.makeText(this, R.string.debug_swipe, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "A llorar que se perdió el tete", Toast.LENGTH_SHORT).show()
//            mDPM.wipeData(flags)
    } else
        Toast.makeText(this, R.string.admin_not_enabled, Toast.LENGTH_SHORT).show()

}

fun Context.isMyServiceRunning(
    serviceClass: Class<*>
): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    @Suppress("DEPRECATION")
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className)
            return true
    }
    return false
}