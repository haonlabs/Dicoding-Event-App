package id.haonlabs.dicodingeventapp.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreference private constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val REMINDER_KEY = booleanPreferencesKey("reminder_setting")

        @Volatile
        private var myInstance: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference =
            myInstance
                ?: synchronized(this) {
                    val instance = SettingPreference(dataStore)
                    myInstance = instance
                    instance
                }
    }

    fun getThemeSetting(): Flow<Boolean> = dataStore.data.map { preferences -> preferences[THEME_KEY] ?: false }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences -> preferences[THEME_KEY] = isDarkModeActive }
    }

    fun getReminderSetting(): Flow<Boolean> = dataStore.data.map { preferences -> preferences[REMINDER_KEY] ?: false }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit { preferences -> preferences[REMINDER_KEY] = isReminderActive }
    }
}
