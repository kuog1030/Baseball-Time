package com.gillian.baseball.team.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.Player
import com.gillian.baseball.databinding.ItemTeammateBinding


class TeammateAdapter(val onClickListener: OnClickListener) : ListAdapter<Player, TeammateAdapter.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (player: Player, position: Int) -> Unit) {
        fun onClick(player: Player, position: Int) = clickListener(player, position)
    }

    class ViewHolder(private var binding: ItemTeammateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.player = player
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemTeammateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(player, position)
        }
        holder.bind(player)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.number == newItem.number
        }

    }

}