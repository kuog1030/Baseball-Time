package com.gillian.baseball.finalgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.data.PitcherBox
import com.gillian.baseball.databinding.ItemPitcherEraBinding

class EraAdapter(val viewModel: FinalViewModel) : ListAdapter<PitcherBox, EraAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemPitcherEraBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pitcher: PitcherBox, viewModel: FinalViewModel) {
            binding.pitcher = pitcher
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EraAdapter.ViewHolder {

        return ViewHolder(ItemPitcherEraBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EraAdapter.ViewHolder, position: Int) {
        val pitcher = getItem(position)
        holder.bind(pitcher, viewModel)

    }


    companion object DiffCallback : DiffUtil.ItemCallback<PitcherBox>() {
        override fun areItemsTheSame(oldItem: PitcherBox, newItem: PitcherBox): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PitcherBox, newItem: PitcherBox): Boolean {
            return oldItem.order == newItem.order
        }
    }
}

