package id.haonlabs.dicodingeventapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.R
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.databinding.ActivitySearchBinding
import id.haonlabs.dicodingeventapp.utils.Result
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.search.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchActivityViewModel by viewModels {
        ViewModelFactory.getInstance(this@SearchActivity)
    }

    companion object {
        const val EXTRA_SEARCH = "extra_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.search)
        }

        val keyword = intent.getStringExtra(EXTRA_SEARCH) ?: ""

        if (savedInstanceState == null) {
            viewModel.searchEvents(keyword)
        }

        with(binding) {
            searchBar.setText(intent.getStringExtra(EXTRA_SEARCH))
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                binding.errorPage.visibility = View.GONE
                searchBar.setText(searchView.text)
                searchView.hide()
                binding.searchView.hide()
                binding.rvSearch.adapter = null
                viewModel.searchEvents(searchBar.text.toString())
                false
            }
        }

        viewModel.listEvents.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.loading.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)
                        val adapter = EventAdapter(listEventData)
                        binding.rvSearch.adapter = adapter
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

        binding.btnTryAgain.setOnClickListener {
            viewModel.searchEvents(binding.searchBar.text.toString())
            binding.errorPage.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onNavigateUp()
    }
}
