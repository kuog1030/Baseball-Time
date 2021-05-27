package com.gillian.baseball

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.data.*
import com.gillian.baseball.team.pager.TeammateAdapter
import com.gillian.baseball.views.BoxAdapter
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

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

@BindingAdapter("hitterBox")
fun bindHitterBox(recyclerView: RecyclerView, hitterBox: List<HitterBox>?) {
    hitterBox?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is HitterBoxAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("pitcherBox")
fun bindPitcherBox(recyclerView: RecyclerView, pitcherBox: List<PitcherBox>?) {
    pitcherBox?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is PitcherBoxAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("gameBox")
fun bindGameBox(recyclerView: RecyclerView, gameBox: List<BoxView>?) {
    gameBox?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is BoxAdapter -> submitList(it)
            }
        }
    }
}


@BindingAdapter("scoresGames")
fun bindScoresGames(recyclerView: RecyclerView, games: List<GameCard>?) {
    games?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is CardScoreAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("atBaseFirst")
fun bindAtBaseFirst(textView: TextView, atBase: AtBase?) {
    atBase?.let{
        if (atBase.base == 1){
            textView.text = atBase.player.name
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("atBaseSecond")
fun bindAtBaseSecond(textView: TextView, atBase: AtBase?) {
    atBase?.let{
        if (atBase.base == 2){
            textView.text = atBase.player.name
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("atBaseThird")
fun bindAtBaseThird(textView: TextView, atBase: AtBase?) {
    atBase?.let{
        if (atBase.base == 3){
            textView.text = atBase.player.name
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("onBaseFirst")
fun bindOnBaseFirst(textView: TextView, onBaseInfo: OnBaseInfo?) {
    onBaseInfo?.let {
        if (onBaseInfo.onClickPlayer == 1) {
            textView.text = (onBaseInfo.baseList[1]?.name) ?: ""
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("onBaseSecond")
fun bindOnBaseSecond(textView: TextView, onBaseInfo: OnBaseInfo?) {
    onBaseInfo?.let {
        if (onBaseInfo.onClickPlayer == 2) {
            textView.text = (onBaseInfo.baseList[1]?.name) ?: ""
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("onBaseThird")
fun bindOnBaseThird(textView: TextView, onBaseInfo: OnBaseInfo?) {
    onBaseInfo?.let {
        if (onBaseInfo.onClickPlayer == 3) {
            textView.text = (onBaseInfo.baseList[1]?.name) ?: ""
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
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

@BindingAdapter("teamImageUrl")
fun bindTeamImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
            .load(url)
            .circleCrop()
            .apply(RequestOptions()
                    .placeholder(R.drawable.baseball_glove)
                    .error(R.drawable.baseball_glove))
            .into(imageView)
}

@BindingAdapter("boxStyle")
fun bindStyleForBox(textView: TextView, isTitle: Boolean) {
    if (isTitle) {
        textView.setTextColor(BaseballApplication.instance.getColor(R.color.white))
    } else {
        textView.setTextColor(BaseballApplication.instance.getColor(R.color.black))
    }
}

@BindingAdapter("overallTeamStat")
fun bindTextAndTeamStat(textView: TextView, number: Int?) {
    number?.let{
        if (number == 0) {
            textView.text = "-"
        } else {
            textView.text = number.toString()
        }
    }
}

@BindingAdapter("loadingStatus")
fun bindLoadingStatus(view: ProgressBar, status: LoadStatus?) {
    view.visibility = when (status) {
        LoadStatus.LOADING -> View.VISIBLE
        else -> View.GONE
    }
}

//
//@BindingAdapter("onFirstBase")
//fun bindFirstBaseRunner(textView: TextView, baseList: Array<EventPlayer?>) {
//    textView.text = if (baseList[1] != null) baseList[1]!!.name else ""
//}