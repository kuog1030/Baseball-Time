package com.gillian.baseball.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.databinding.ItemHitterBoxBinding


class HitterBoxAdapter : ListAdapter<HitterBox, HitterBoxAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemHitterBoxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(isTitle: Boolean, isEven: Boolean, hitter: HitterBox) {
            binding.isTitle = isTitle
            binding.isEven = isEven
            binding.hitter = hitter
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemHitterBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hitter = getItem(position)
        val isTitle = (position == 0)
        holder.bind(isTitle, (position % 2 == 0), hitter)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HitterBox>() {
        override fun areItemsTheSame(oldItem: HitterBox, newItem: HitterBox): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HitterBox, newItem: HitterBox): Boolean {
            return oldItem.playerId == newItem.playerId
        }

    }
}