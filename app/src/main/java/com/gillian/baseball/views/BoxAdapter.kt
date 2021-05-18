package com.gillian.baseball.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.BoxView
import com.gillian.baseball.databinding.ItemBoxBinding


class BoxAdapter : ListAdapter<BoxView, BoxAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemBoxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(box: BoxView) {
            binding.box = box
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val box = getItem(position)
        holder.bind(box)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<BoxView>() {
        override fun areItemsTheSame(oldItem: BoxView, newItem: BoxView): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BoxView, newItem: BoxView): Boolean {
            return oldItem == newItem
        }

    }

}