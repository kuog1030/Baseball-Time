package com.gillian.baseball.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.PersonalScore
import com.gillian.baseball.databinding.ItemPersonalScoreBinding


class PersonalScoreAdapter : ListAdapter<PersonalScore, PersonalScoreAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemPersonalScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(score: PersonalScore) {
            binding.score = score
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemPersonalScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = getItem(position)
        holder.bind(score)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PersonalScore>() {
        override fun areItemsTheSame(oldItem: PersonalScore, newItem: PersonalScore): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PersonalScore, newItem: PersonalScore): Boolean {
            return oldItem == newItem
        }

    }
}