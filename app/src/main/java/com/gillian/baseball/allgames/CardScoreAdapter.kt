package com.gillian.baseball.allgames

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.GameStatus
import com.gillian.baseball.databinding.ItemCardScheduleBinding
import com.gillian.baseball.databinding.ItemCardScoreBinding
import com.gillian.baseball.databinding.ItemCardSmallBinding


class CardScoreAdapter(val onClickListener: CardScoreAdapter.OnClickListener, val isBroadcast: Boolean = false, val viewModel: AllGamesViewModel? = null)
    : ListAdapter<GameCard, RecyclerView.ViewHolder>(DiffCallback) {

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

    class ScheduleViewHolder(private var binding: ItemCardScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: GameCard, onClickListener: OnClickListener, viewModel: AllGamesViewModel?) {
            binding.game = game
            binding.buttonCardScheduleStart.setOnClickListener { onClickListener.onClick(game) }
            binding.buttonScheduleMore.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.layoutCardDetail.visibility = View.VISIBLE
                } else {
                    binding.layoutCardDetail.visibility = View.GONE
                }
            }
            binding.executePendingBindings()

            if (viewModel != null) {
                binding.buttonScheduleDelete.setOnClickListener {
                    binding.progressScheduleDelete.visibility = View.VISIBLE
                    viewModel.deleteGame(game.id)
                }
            }
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
            TYPE_SCHEDULE -> ScheduleViewHolder(ItemCardScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_FULL -> ViewHolder(ItemCardScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_SMALL -> SmallViewHolder(ItemCardSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(game, onClickListener)
            is SmallViewHolder -> holder.bind(game, onClickListener)
            is ScheduleViewHolder -> holder.bind(game, onClickListener, viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isBroadcast) {
            return (TYPE_SMALL)
        } else {
            return when (getItem(position).status) {
                GameStatus.SCHEDULED.number -> TYPE_SCHEDULE
                else -> when (position) {
                    0 -> TYPE_FULL
                    1 -> TYPE_SMALL
                    else -> TYPE_SMALL
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GameCard>() {
        override fun areItemsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GameCard, newItem: GameCard): Boolean {
            return oldItem.id == newItem.id
        }

        private const val TYPE_SCHEDULE = 0x00
        private const val TYPE_FULL = 0x01
        private const val TYPE_SMALL = 0x02
    }
}

