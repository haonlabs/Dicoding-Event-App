package id.haonlabs.dicodingeventapp.ui.detail

import android.content.Intent
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.haonlabs.dicodingeventapp.R
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEvent
import id.haonlabs.dicodingeventapp.databinding.ActivityDetailBinding
import id.haonlabs.dicodingeventapp.utils.Result
import id.haonlabs.dicodingeventapp.utils.loadImage
import id.haonlabs.dicodingeventapp.viewmodel.ViewModelFactory
import id.haonlabs.dicodingeventapp.viewmodel.detail.DetailActivityViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.detailDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: DetailActivityViewModel by viewModels { factory }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Event"
        }

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        if (savedInstanceState == null) {
            viewModel.getDetailEvent(eventId)
        }

        viewModel.event.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.loading.visibility = View.GONE
                        val eventData = result.data
                        supportActionBar?.title = eventData.name

                        binding.apply {
                            viewModel.getFavoriteEventById(eventData.id).observe(
                                this@DetailActivity,
                            ) { favoriteEvent ->
                                val favoriteEventData =
                                    FavoriteEvent(
                                        eventData.id,
                                        eventData.name,
                                        eventData.mediaCover,
                                        eventData.imageLogo,
                                        eventData.summary,
                                        eventData.link,
                                    )
                                if (favoriteEvent != null) {
                                    binding.detailFabFavorite.setImageResource(
                                        R.drawable.ic_favorite_red,
                                    )
                                    detailFabFavorite.setOnClickListener {
                                        viewModel.delete(favoriteEventData)
                                    }
                                } else {
                                    binding.detailFabFavorite.setImageResource(
                                        R.drawable.ic_favorite_black,
                                    )
                                    detailFabFavorite.setOnClickListener {
                                        viewModel.insert(favoriteEventData)
                                    }
                                }
                            }
                            detailImg.loadImage(eventData.mediaCover)
                            binding.detailName.text = eventData.name
                            binding.detailOwnerName.text =
                                getString(R.string.penyelenggara, eventData.ownerName)
                            binding.detailTime.text =
                                getString(
                                    R.string.waktu,
                                    convertToHumanReadable(eventData.beginTime),
                                )
                            binding.detailQuota.text =
                                getString(
                                    R.string.sisa_kuota,
                                    String.format(
                                        Locale.getDefault(),
                                        "%d",
                                        eventData.quota - eventData.registrants,
                                    ),
                                )

                            binding.detailDesc.text =
                                HtmlCompat.fromHtml(
                                    eventData.description,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY,
                                )
                            binding.detailFabFavorite.visibility = View.VISIBLE
                            binding.detailRegister.visibility = View.VISIBLE
                            binding.errorPage.visibility = View.GONE

                            binding.detailRegister.setOnClickListener {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(eventData.link)).apply {
                                        addCategory(Intent.CATEGORY_BROWSABLE)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                startActivity(intent)
                            }
                        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToHumanReadable(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        return dateTime.format(humanReadableFormatter)
    }
}
