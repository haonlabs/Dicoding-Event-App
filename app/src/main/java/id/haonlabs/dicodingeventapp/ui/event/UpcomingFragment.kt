package id.haonlabs.dicodingeventapp.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.databinding.FragmentUpcomingBinding
import id.haonlabs.dicodingeventapp.utils.Result
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.event.UpcomingFragmentViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val viewModel: UpcomingFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getUpcomingEvent(40)
        }

        viewModel.listEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.loading.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireActivity())
                        val adapter = EventAdapter(listEventData)
                        binding.rvUpcoming.adapter = adapter
                        binding.errorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.loading.visibility = View.GONE
                        binding.errorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            viewModel.getUpcomingEvent(40)
            binding.errorPage.visibility = View.GONE
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
