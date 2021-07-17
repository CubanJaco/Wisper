package cu.jaco.wisper.repositories.preferences

import android.content.Context
import android.content.SharedPreferences


object AppPreferences {

    lateinit var dataStore: AppPreferenceDataStore
    private val sharedPreferences: SharedPreferences
        get() = dataStore.preferences

    fun init(context: Context) {
        if (!::dataStore.isInitialized)
            dataStore = AppPreferenceDataStore().apply {
                init(context)
            }
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = this.edit()
        editor.body()
        editor.apply()
        editor.commit()
    }

    var firstTimeOpen: Boolean
        get() = sharedPreferences.getBoolean("firstTimeOpen", true)
        set(value) = sharedPreferences.put {
            putBoolean("firstTimeOpen", value)
        }

    var clearExternalStorage: Boolean
        get() = sharedPreferences.getBoolean("clearExternalStorage", false)
        set(value) = sharedPreferences.put {
            putBoolean("clearExternalStorage", value)
        }

}