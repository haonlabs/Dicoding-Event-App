package id.haonlabs.dicodingeventapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import id.haonlabs.dicodingeventapp.R
import id.haonlabs.dicodingeventapp.adapter.EventAdapter
import id.haonlabs.dicodingeventapp.databinding.ActivitySearchBinding
import id.haonlabs.dicodingeventapp.viewmodel.search.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchActivityViewModel by viewModels()

    companion object {
        const val EXTRA_SEARCH = "extra_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Search Event"
        }

        val keyword = intent.getStringExtra(EXTRA_SEARCH) ?: ""

        if (savedInstanceState == null) {
            viewModel.searchEvent(keyword)
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
                viewModel.searchEvent(searchBar.text.toString())
                false
            }
        }

        viewModel.listEvent.observe(this) {
            binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)
            val adapter = EventAdapter(it)
            binding.rvSearch.adapter = adapter
            binding.errorPage.visibility = View.GONE
        }

        viewModel.resultText.observe(this) {
            Log.d("SearchActivity", "onCreate: $it")
            binding.searchNotFound.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            binding.searchNotFound.text = it
        }

        viewModel.isLoading.observe(this) {
            Log.d("isLoading", "onCreate: $it")
            binding.loading.isVisible = it
        }

        viewModel.errorMessage.observe(this) {
            Log.d("errMsg", "onCreate: $it")
            binding.errorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.searchEvent(binding.searchBar.text.toString())
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
