package id.haonlabs.dicodingeventapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.databinding.FragmentFavoriteBinding
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.favorite.FavoriteFragmentViewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: FavoriteFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getFavoriteEvents()
        }

        viewModel.favoriteEvent.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                binding.progressBar.visibility = View.GONE
                binding.rvFavorite.layoutManager = LinearLayoutManager(requireActivity())
                val items = arrayListOf<ListEventsItem>()
                result.forEach {
                    items.add(
                        ListEventsItem(
                            id = it.id,
                            name = it.name,
                            summary = it.summary,
                            imageLogo = it.imageLogo!!,
                            mediaCover = it.mediaCover!!,
                            registrants = 0,
                            link = "",
                            description = "",
                            ownerName = "",
                            cityName = "",
                            quota = 0,
                            beginTime = "",
                            endTime = "",
                            category = "",
                        ),
                    )
                }
                val adapter = EventAdapter(items)
                binding.rvFavorite.adapter = adapter
                binding.errorPage.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            viewModel.getFavoriteEvents()
            binding.errorPage.visibility = View.GONE
        }

        return root
    }
}
