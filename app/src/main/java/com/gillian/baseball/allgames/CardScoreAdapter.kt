package com.gillian.baseball.allgames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.databinding.ItemCardScoreBinding
import com.gillian.baseball.game.order.PinchAdapter
import com.gillian.baseball.views.PersonalScoreAdapter

class CardScoreAdapter(val onClickListener: CardScoreAdapter.OnClickListener) : ListAdapter<GameCard, CardScoreAdapter.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (gameCard: GameCard) -> Unit) {
        fun onClick(gameCard: GameCard) = clickListener(gameCard)
    }

    class ViewHolder(private var binding: ItemCardScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: GameCard) {
            binding.game = game

            //TODO() optional
            //binding.recyclerGameEnd.adapter = PersonalScoreAdapter()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemCardScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(game)
        }
        holder.bind(game)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GameCard>() {
        override fun areItemsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem.id == newItem.id
        }

    }
}