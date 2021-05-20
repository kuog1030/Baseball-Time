package com.gillian.baseball.allgames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.ItemCardScoreBinding

class CardScoreAdapter : ListAdapter<Game, CardScoreAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemCardScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.game = game
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemCardScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

    }
}