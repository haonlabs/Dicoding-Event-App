package id.haonlabs.dicodingeventapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.databinding.HorizontalRowEventBinding
import id.haonlabs.dicodingeventapp.databinding.ItemRowEventBinding
import id.haonlabs.dicodingeventapp.ui.detail.DetailActivity
import id.haonlabs.dicodingeventapp.utils.loadImage

class EventAdapter(
    private val listEvent: List<ListEventsItem>,
    private val horizontal: Boolean = false,
) : RecyclerView.Adapter<EventAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            (if (horizontal)
                HorizontalRowEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            else ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listEvent[position]
        if (horizontal) {
            val binding = holder.binding as HorizontalRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardSummary.text = data.summary
            binding.cardCover.loadImage(data.imageLogo)
        } else {
            val binding = holder.binding as ItemRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardSummary.text = data.summary
            binding.cardCover.loadImage(data.imageLogo)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, data.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listEvent.size
}
