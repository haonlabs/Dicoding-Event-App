package id.haonlabs.dicodingeventapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.haonlabs.dicodingeventapp.databinding.ActivityMainBinding
import id.haonlabs.dicodingeventapp.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished)
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.search_btn) {
            binding.searchView.show()

            binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
                binding.searchView.hide()
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra(SearchActivity.EXTRA_SEARCH, binding.searchView.text.toString())
                startActivity(intent)
                false
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
