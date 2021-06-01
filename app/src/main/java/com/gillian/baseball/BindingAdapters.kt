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
import com.gillian.baseball.broadcast.BroadcastAdapter
import com.gillian.baseball.data.*
import com.gillian.baseball.loginflow.SearchTeamAdapter
import com.gillian.baseball.team.pager.TeammateAdapter
import com.gillian.baseball.views.BoxAdapter
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PersonalScoreAdapter
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

@BindingAdapter("searchTeam")
fun bindSearchTeam(recyclerView: RecyclerView, teams: List<Team>?) {
    teams?.let{
        recyclerView.adapter?.apply {
            when (this) {
                is SearchTeamAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("personalBox")
fun bindPersonalBox(recyclerView: RecyclerView, myScore: List<PersonalScore>?) {
    myScore?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is PersonalScoreAdapter -> submitList(it)
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

@BindingAdapter("orderText")
fun bindOrderToText(textView: TextView, order: Int?) {
    order?.let{
        textView.text = when (order % 100) {
            0 -> (order / 100).toString()
            else -> ""
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

@BindingAdapter("liveEvents")
fun bindLiveEvents(recyclerView: RecyclerView, events: List<Event>?) {
    events?.let{
        recyclerView.adapter?.apply {
            when(this) {
                is BroadcastAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("gameResult")
fun bindGameResultToText(textView: TextView, result: GameResult?) {
    result?.let{
        when (result) {
            GameResult.WIN -> {
                textView.text = "勝"
                textView.setTextColor(BaseballApplication.instance.getColor(R.color.hit_block))
            }
            GameResult.LOSE -> {
                textView.text = "敗"
            }
            else -> {
                textView.text = "和"
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

@BindingAdapter("photoUrl")
fun bindProfilePhoto(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
            .load(url)
            .circleCrop()
            .into(imageView)
}

@BindingAdapter("defaultPhoto")
fun bindDefaultPhoto(textView: TextView, name: String) {
    textView.text = name.substring(0, 1)
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

@BindingAdapter("personalScoreStyle")
fun bindStyleForPersonalScore(textView: TextView, color: Int) {
    when (color) {
        0 -> textView.setBackgroundColor(BaseballApplication.instance.getColor(R.color.hit_block))
        1 -> textView.setBackgroundColor(BaseballApplication.instance.getColor(R.color.out_block))
        2 -> textView.setBackgroundColor(BaseballApplication.instance.getColor(R.color.safe_block))
        else -> textView.setBackgroundColor(BaseballApplication.instance.getColor(R.color.out_block))
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

@BindingAdapter("inningCount")
fun bindTextInning(textView: TextView, inning: Int) {
    val inningNine = (inning + 1 ) / 2
    if (inning % 2 == 1) {
        textView.text = BaseballApplication.instance.getString(R.string.inning_top, inningNine)
    } else {
        textView.text = BaseballApplication.instance.getString(R.string.inning_bottom, inningNine)
    }
}

@BindingAdapter("teamCode")
fun bindTextTeamCode(textView: TextView, teamId: String?) {
    teamId?.let{
        textView.text = it.substring(0, 5)
    }
}

@BindingAdapter("loadingStatus")
fun bindLoadingStatus(view: View, status: LoadStatus?) {
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