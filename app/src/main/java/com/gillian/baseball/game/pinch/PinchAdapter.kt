package com.gillian.baseball.game.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.gillian.baseball.data.EventPlayer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.databinding.ItemOrderPitcherBinding
import com.gillian.baseball.databinding.ItemOrderPlayerBinding

class PinchAdapter(val onClickListener: OnClickListener ) : ListAdapter<EventPlayer, PinchAdapter.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (player: EventPlayer, position: Int) -> Unit) {
        fun onClick(player: EventPlayer, position: Int) = clickListener(player, position)
    }

    class ViewHolder(private var binding: ItemOrderPitcherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: EventPlayer) {
            binding.player = player
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemOrderPitcherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(player, position)
        }
        holder.bind(player)
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