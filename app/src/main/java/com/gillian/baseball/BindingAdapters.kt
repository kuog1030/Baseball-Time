package com.gillian.baseball

import android.widget.TextView
import androidx.databinding.BindingAdapter

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