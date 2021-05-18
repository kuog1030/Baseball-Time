package com.gillian.baseball

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Player
import com.gillian.baseball.team.pager.TeammateAdapter

@BindingAdapter("ballCount")
fun bindBallCount(textView: TextView, count: Int?) {
    count?.let {
        textView.text = when (count) {
            0 -> BaseballApplication.instance.getString(R.string.three_zero)
            1 -> BaseballApplication.instance.getString(R.string.three_one)
            2 -> BaseballApplication.instance.getString(R.string.three_two)
            else -> BaseballApplication.instance.getString(R.string.three_three)
        }
    }
}

@BindingAdapter("twoCount")
fun bindStrikeCount(textView: TextView, count: Int?) {
    count?.let {
        textView.text = when (count) {
            0 -> BaseballApplication.instance.getString(R.string.two_zero)
            1 -> BaseballApplication.instance.getString(R.string.two_one)
            else -> BaseballApplication.instance.getString(R.string.two_two)
        }
    }
}

@BindingAdapter("teammates")
fun bindTeamPlayer(recyclerView: RecyclerView, teamPlayers: MutableList<Player>?) {
    teamPlayers?.let{
        recyclerView.adapter?.apply {
            when (this) {
                is TeammateAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun bindProfileImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
            .load(url)
            .circleCrop()
            .apply(RequestOptions()
                    .placeholder(R.drawable.ic_baseline_sports_baseball_24)
                    .error(R.drawable.ic_baseline_sports_baseball_24))
            .into(imageView)
}

//
//@BindingAdapter("onFirstBase")
//fun bindFirstBaseRunner(textView: TextView, baseList: Array<EventPlayer?>) {
//    textView.text = if (baseList[1] != null) baseList[1]!!.name else ""
//}