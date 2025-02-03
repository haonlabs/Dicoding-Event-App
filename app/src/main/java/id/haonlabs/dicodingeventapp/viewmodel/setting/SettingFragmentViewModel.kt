package id.haonlabs.dicodingeventapp.viewmodel.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.haonlabs.dicodingeventapp.data.local.preference.SettingPreference
import kotlinx.coroutines.launch

class SettingFragmentViewModel(
    private val pref: SettingPreference,
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDarkModeActive) }
    }

    fun getReminderSettings(): LiveData<Boolean> = pref.getReminderSetting().asLiveData()

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch { pref.saveReminderSetting(isReminderActive) }
    }
}
