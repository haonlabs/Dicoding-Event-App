package id.haonlabs.dicodingeventapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.databinding.FragmentHomeBinding
import id.haonlabs.dicodingeventapp.viewmodel.event.FinishedFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.event.UpcomingFragmentViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val upcomingViewModel: UpcomingFragmentViewModel by viewModels()
    private val finishedViewModel: FinishedFragmentViewModel by viewModels()

    private val limit = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            finishedViewModel.getEvents(limit)
            upcomingViewModel.getEvents(limit)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFinishedEvent()
        getUpcomingEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            upcomingViewModel.getEvents(limit)
            binding.upcomingErrorPage.visibility = View.GONE
        }
        binding.finishedBtnTryAgain.setOnClickListener {
            finishedViewModel.getEvents(limit)
            binding.finishedErrorPage.visibility = View.GONE
        }

        return root
    }

    private fun getFinishedEvent() {

        finishedViewModel.listEvent.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getFinishedEvent: $it")
            binding.rvFinished.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = EventAdapter(it)
            binding.rvFinished.adapter = adapter
            binding.finishedErrorPage.visibility = View.GONE
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getFinishedEvent: $it")
            binding.finishedProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getFinishedEvent: $it")
            binding.finishedErrorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.finishedErrorMessage.text = it
        }
    }

    private fun getUpcomingEvent() {

        upcomingViewModel.listEvent.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getUpcomingEvent: $it")
            binding.rvUpcoming.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            val adapter = EventAdapter(it, horizontal = true)
            binding.rvUpcoming.adapter = adapter
            binding.upcomingErrorPage.visibility = View.GONE
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getUpcomingEvent: $it")
            binding.upcomingProgressBar.isVisible = it
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "getUpcomingEvent: $it")
            binding.upcomingErrorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
