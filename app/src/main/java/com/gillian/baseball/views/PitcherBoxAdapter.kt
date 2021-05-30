package com.gillian.baseball.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.PitcherBox
import com.gillian.baseball.databinding.ItemPitcherBoxBinding

class PitcherBoxAdapter : ListAdapter<PitcherBox, PitcherBoxAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemPitcherBoxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(isTitle: Boolean, pitcher: PitcherBox) {
            binding.isTitle = isTitle
            binding.pitcher = pitcher
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemPitcherBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pitcher = getItem(position)
        val isTitle = (position == 0)
        holder.bind(isTitle, pitcher)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PitcherBox>() {
        override fun areItemsTheSame(oldItem: PitcherBox, newItem: PitcherBox): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PitcherBox, newItem: PitcherBox): Boolean {
            // TODO 原本是判斷oldItem.playerId 確實會不同但感覺很浪費時間判斷很多字串?
            return oldItem.order == newItem.order
        }

    }
}