package com.gillian.baseball.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.gillian.baseball.data.EventPlayer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.databinding.ItemOrderPlayerBinding

class OrderAdapter : ListAdapter<EventPlayer, OrderAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemOrderPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: EventPlayer, isChecked: Boolean, position: Int) {
            binding.player = player
            binding.isChecked = isChecked
            binding.order = if (isChecked) (position+1).toString() else ""
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemOrderPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = getItem(position)
        holder.bind(player, (position < 9), position)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EventPlayer>() {
        override fun areItemsTheSame(oldItem: EventPlayer, newItem: EventPlayer): Boolean {
            return oldItem === newItem
        }

        // TODO() 這個判斷要改掉
        override fun areContentsTheSame(oldItem: EventPlayer, newItem: EventPlayer): Boolean {
            return oldItem.number == newItem.number
        }

    }

}