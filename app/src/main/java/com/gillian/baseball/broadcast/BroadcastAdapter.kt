package com.gillian.baseball.broadcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.Event
import com.gillian.baseball.databinding.ItemBroadcastBinding
import com.gillian.baseball.databinding.ItemBroadcastInningBinding
import com.gillian.baseball.game.EventType

class BroadcastAdapter : ListAdapter<Event, RecyclerView.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemBroadcastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            binding.executePendingBindings()
        }
    }

    class InningViewHolder(private var binding: ItemBroadcastInningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.inning = event.inning
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EVENT -> ViewHolder(ItemBroadcastBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_INNING -> InningViewHolder(ItemBroadcastInningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(event)
            is InningViewHolder -> holder.bind(event)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).result == EventType.INNINGCHANGE.number) {
            return (TYPE_INNING)
        } else {
            return (TYPE_EVENT)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
        private const val TYPE_EVENT = 0x07
        private const val TYPE_INNING = 0x08
    }

}