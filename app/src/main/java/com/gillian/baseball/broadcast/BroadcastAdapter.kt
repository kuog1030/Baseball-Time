package com.gillian.baseball.broadcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.Event
import com.gillian.baseball.databinding.ItemBroadcastBinding
import com.gillian.baseball.databinding.ItemBroadcastInningBinding
import com.gillian.baseball.databinding.ItemBroadcastTextBinding
import com.gillian.baseball.game.EventType

class BroadcastAdapter : ListAdapter<Event, RecyclerView.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemBroadcastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            binding.executePendingBindings()
        }
    }

    class InningViewHolder(private var binding: ItemBroadcastInningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            if (event.result == EventType.IPEND.number) {
                binding.inning = -1
            } else {
                binding.inning = event.inning
            }
            binding.executePendingBindings()
        }
    }

    class TextViewHolder(private var binding: ItemBroadcastTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventString = event.toOnBaseString()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EVENT -> ViewHolder(ItemBroadcastBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_INNING -> InningViewHolder(ItemBroadcastInningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_TEXT -> TextViewHolder(ItemBroadcastTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(event)
            is InningViewHolder -> holder.bind(event)
            is TextViewHolder -> holder.bind(event)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).result) {
            EventType.INNINGCHANGE.number -> TYPE_INNING
            EventType.IPEND.number -> TYPE_INNING          // 最後一個出局數結束
            EventType.INNINGSPITCHED.number -> TYPE_TEXT // 換投
            EventType.STEALBASEFAIL.number -> TYPE_TEXT  // 盜壘失敗
            EventType.PICKOFF.number -> TYPE_TEXT    // 牽制出局
            EventType.STEALBASE.number -> TYPE_TEXT  // 盜壘成功
            EventType.ERROR.number -> TYPE_TEXT      // 發生失誤
            EventType.ONBASEOUT.number -> TYPE_TEXT  // 壘包上的出局 如雙殺
            else -> TYPE_EVENT
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
        private const val TYPE_EVENT = 0x07
        private const val TYPE_INNING = 0x08
        private const val TYPE_TEXT = 0x09
    }

}