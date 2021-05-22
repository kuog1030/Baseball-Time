package com.gillian.baseball.allgames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.databinding.ItemCardScoreBinding
import com.gillian.baseball.databinding.ItemCardSmallBinding


class CardScoreAdapter(val onClickListener: CardScoreAdapter.OnClickListener) : ListAdapter<GameCard, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (gameCard: GameCard) -> Unit) {
        fun onClick(gameCard: GameCard) = clickListener(gameCard)
    }

    class SmallViewHolder(private var binding: ItemCardSmallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: GameCard, onClickListener: OnClickListener) {
            binding.game = game
            binding.root.setOnClickListener { onClickListener.onClick(game) }
            binding.executePendingBindings()
        }
    }

    class ViewHolder(private var binding: ItemCardScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: GameCard, onClickListener: OnClickListener) {
            binding.game = game
            binding.root.setOnClickListener { onClickListener.onClick(game) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_VIEW_TYPE_FULL -> ViewHolder(ItemCardScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_SMALL -> SmallViewHolder(ItemCardSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(game, onClickListener)
            is SmallViewHolder -> holder.bind(game, onClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_VIEW_TYPE_FULL
            1 -> ITEM_VIEW_TYPE_FULL
            else -> ITEM_VIEW_TYPE_SMALL
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GameCard>() {
        override fun areItemsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_FULL = 0x00
        private const val ITEM_VIEW_TYPE_SMALL = 0x01
    }
}

