package id.haonlabs.dicodingeventapp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import id.haonlabs.dicodingeventapp.R
import id.haonlabs.dicodingeventapp.databinding.ActivityDetailBinding
import id.haonlabs.dicodingeventapp.utils.loadImage
import id.haonlabs.dicodingeventapp.viewmodel.detail.DetailActivityViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.detailDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Event"
        }

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        if (savedInstanceState == null) {
            viewModel.getDetailEvent(eventId)
        }

        viewModel.event.observe(this) {
            supportActionBar?.title = it.name

            binding.apply {
                detailImg.loadImage(it.mediaCover)
                binding.detailName.text = it.name
                binding.detailOwnerName.text = getString(R.string.penyelenggara, it.ownerName)
                binding.detailTime.text =
                    getString(R.string.waktu, convertToHumanReadable(it.beginTime))
                binding.detailQuota.text =
                    getString(
                        R.string.sisa_kuota,
                        String.format(Locale.getDefault(), "%d", it.quota - it.registrants),
                    )

                binding.detailDesc.text =
                    HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                binding.detailRegister.visibility = View.VISIBLE
                binding.errorPage.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { binding.loading.isVisible = it }

        binding.detailRegister.setOnClickListener {
            val url = "https://www.dicoding.com/events/${eventId}"
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this) {
            binding.errorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getDetailEvent(eventId)
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

        return super.onSupportNavigateUp()
    }

    @SuppressLint("NewApi")
    private fun convertToHumanReadable(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        return dateTime.format(humanReadableFormatter)
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
