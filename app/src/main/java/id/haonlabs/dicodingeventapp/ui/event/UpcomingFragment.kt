package id.haonlabs.dicodingeventapp.ui.event

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
import id.haonlabs.dicodingeventapp.databinding.FragmentUpcomingBinding
import id.haonlabs.dicodingeventapp.viewmodel.event.UpcomingFragmentViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val viewModel: UpcomingFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listEvent.observe(viewLifecycleOwner) {
            binding.rvUpcoming.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = EventAdapter(it)
            binding.rvUpcoming.adapter = adapter
            binding.errorPage.visibility = View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { binding.loading.isVisible = it }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.errorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.d("cek saved", savedInstanceState.toString())
        if (savedInstanceState == null) {
            viewModel.getEvents(40)
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getEvents()
            binding.errorPage.visibility = View.GONE
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
