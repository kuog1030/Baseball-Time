package com.gillian.baseball.team.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.Rank
import com.gillian.baseball.databinding.ItemRankBinding

class RankAdapter : ListAdapter<Rank, RankAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rank: Rank) {
            binding.rank = rank
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rank = getItem(position)
        holder.bind(rank)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Rank>() {
        override fun areItemsTheSame(oldItem: Rank, newItem: Rank): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Rank, newItem: Rank): Boolean {
            return oldItem == newItem
        }

    }

}