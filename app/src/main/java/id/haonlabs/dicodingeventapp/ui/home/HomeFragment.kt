package id.haonlabs.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.databinding.FragmentHomeBinding
import id.haonlabs.dicodingeventapp.utils.Result
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.event.FinishedFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.event.UpcomingFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.setting.SettingFragmentViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val upcomingViewModel: UpcomingFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val finishedViewModel: FinishedFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val settingsViewModel: SettingFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val limit = 5

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            finishedViewModel.getFinishedEvent(limit)
            upcomingViewModel.getUpcomingEvent(limit)
        }

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                },
            )
        }

        getFinishedEvent()
        getUpcomingEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            upcomingViewModel.getUpcomingEvent(limit)
            binding.upcomingErrorPage.visibility = View.GONE
        }
        binding.finishedBtnTryAgain.setOnClickListener {
            finishedViewModel.getFinishedEvent(limit)
            binding.finishedErrorPage.visibility = View.GONE
        }

        return root
    }

    private fun getFinishedEvent() {
        finishedViewModel.listEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.finishedProgressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.finishedProgressBar.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvFinished.layoutManager = LinearLayoutManager(requireActivity())
                        val adapter = EventAdapter(listEventData)
                        binding.rvFinished.adapter = adapter
                        binding.finishedErrorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.finishedProgressBar.visibility = View.GONE
                        binding.finishedErrorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }
            }
        }
    }

    private fun getUpcomingEvent() {
        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.upcomingProgressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.upcomingProgressBar.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvUpcoming.layoutManager =
                            LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.HORIZONTAL,
                                false,
                            )
                        val adapter = EventAdapter(listEventData, horizontal = true)
                        binding.rvUpcoming.adapter = adapter
                        binding.upcomingErrorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.upcomingProgressBar.visibility = View.GONE
                        binding.upcomingErrorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }
            }
        }
    }
}
