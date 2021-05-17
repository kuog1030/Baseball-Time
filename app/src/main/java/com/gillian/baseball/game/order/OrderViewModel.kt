package com.gillian.baseball.game.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import java.util.*


class OrderViewModel(private val repository: BaseballRepository) : ViewModel() {

    val selectedSideRadio = MutableLiveData<Int>()
    val gameTitle = MutableLiveData<String>()
    val awayTeamName = MutableLiveData<String>()
    val pitcher = MutableLiveData<String>()

    // line up player
//    val first = MutableLiveData<String>()
//    val second = MutableLiveData<String>()
//    val third = MutableLiveData<String>()
//    val forth = MutableLiveData<String>()
//    val fifth = MutableLiveData<String>()
//    val sixth = MutableLiveData<String>()
//    val seventh = MutableLiveData<String>()
//    val eighth = MutableLiveData<String>()
//    val ninth = MutableLiveData<String>()

    var lineUp = mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"),
            EventPlayer("0032","蘇智傑", "32"),
            EventPlayer("0013", "陳鏞基", "13"),
            EventPlayer("0077", "林安可", "77"),
            EventPlayer("0065", "陳重羽", "65"),
            EventPlayer("0064", "林靖凱", "64"),
            EventPlayer("0052", "張偉聖", "52"),
            EventPlayer("0031", "林岱安", "31"),
            EventPlayer("0039", "林祖傑", "39")
    )
    val awayLineUp = mutableListOf<EventPlayer>()


    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_order_home -> true
            else -> false
        }

    private val _navigateToGame = MutableLiveData<MyGame>()
    val navigateToGame : LiveData<MyGame>
        get() = _navigateToGame



    fun navigateToGame() {
        val game = Game(
                name = gameTitle.value ?: "世界第一武道大會",
                date = Calendar.getInstance().timeInMillis,
                place = "")

        //val nameList = listOf(first.value, second.value, third.value, forth.value, fifth.value, sixth.value, seventh.value, eighth.value, ninth.value)
        // TODO() 這是為了debug方便先給預設值，到時候要刪掉
//        val nameList = listOf(first.value ?: "陳傑憲",
//                second.value ?: "蘇智傑",
//                third.value ?: "林安可",
//                forth.value ?: "陳鏞基",
//                fifth.value ?: "林子豪",
//                sixth.value ?: "陳重羽",
//                seventh.value ?: "張偉聖",
//                eighth.value ?: "劉芙豪",
//                ninth.value ?: "林靖凱")
//        val lineUp = mutableListOf<EventPlayer>()
//        val awayLineUp = mutableListOf<EventPlayer>()

//        for ((index, name) in nameList.withIndex()) {
//            if ( name != null ){
//                lineUp.add(EventPlayer(name = name))
//                awayLineUp.add(EventPlayer(name = "第${index+1}棒"))
//            } else {
//                break
//            }
//        }
        for (index in 1..9) {
            awayLineUp.add(EventPlayer(name = "第${index}棒"))
        }

        val myTeam = GameTeam(
                name = "Android",
                teamId = "999",
                pitcher = EventPlayer(name = pitcher.value ?: "古林睿揚", number = "19", userId = "0019"),
                lineUp = lineUp
        )

        val awayTeam = GameTeam(
                name = awayTeamName.value ?: "iOS",
                pitcher = EventPlayer(name = "對方投手"),
                lineUp = awayLineUp
        )

        game.home = if (isHome) myTeam else awayTeam
        game.guest = if (isHome) awayTeam else myTeam

        _navigateToGame.value = MyGame(isHome = isHome, game = game)
    }

    fun onGameNavigated() {
        _navigateToGame.value = null
    }

}