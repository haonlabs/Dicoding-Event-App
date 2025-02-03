package id.haonlabs.dicodingeventapp.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import id.haonlabs.dicodingeventapp.databinding.FragmentSettingBinding
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.setting.SettingFragmentViewModel
import id.haonlabs.dicodingeventapp.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    private val viewModel: SettingFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }

        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchDailyReminder.isChecked = isReminderActive
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        binding.switchDailyReminder.setOnCheckedChangeListener {
            _: CompoundButton?,
            isChecked: Boolean,
            ->
            if (isChecked) {
                workManager.pruneWork()
                val state = workManager.getWorkInfosByTag(WORKER_TAG).get()
                if (state.isEmpty()) {
                    startPeriodicTask()
                }
            } else {
                cancelPeriodicTask()
            }
        }

        workManager = WorkManager.getInstance(requireActivity())

        return root
    }

    private fun startPeriodicTask() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        periodicWorkRequest =
            PeriodicWorkRequest
                .Builder(ReminderWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
        workManager.enqueue(periodicWorkRequest)
        viewModel.saveReminderSetting(true)
    }

    private fun cancelPeriodicTask() {
        workManager.cancelAllWorkByTag(WORKER_TAG)
        viewModel.saveReminderSetting(false)
    }

    companion object {
        private const val WORKER_TAG = "DAILY_REMINDER"
    }
}
