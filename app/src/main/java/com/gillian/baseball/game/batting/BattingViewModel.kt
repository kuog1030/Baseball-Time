package com.gillian.baseball.game.batting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository

class BattingViewModel(private val repository: BaseballRepository) : ViewModel() {

    var ballCount = MutableLiveData<Int>().apply {
        value = 0
    }
    var strikeCount = MutableLiveData<Int>().apply{
        value = 0
    }
    var outCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val lineup = mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"),
            EventPlayer("0032","蘇智傑", "32"),
            EventPlayer("0013", "陳鏞基", "13"),
            EventPlayer("0077", "林安可", "77"),
            EventPlayer("0065", "陳重羽", "65"))

    // 進到dialog的時候帶過去的用球數
    lateinit var hitterEvent : Event

    var baseList = arrayOf<EventPlayer?>(lineup[0], null, null, null)
    var atBaseList = mutableListOf<AtBase>()
    //val newAtBase = arrayOf<EventPlayer?>(null, null, null, null)

    var atBatNumber = 0
    var atBatName = MutableLiveData<String>().apply{
        value = "第1棒 ${baseList[0]?.name}"
    }

    private val _navigateToEvent = MutableLiveData<List<AtBase>>()
    val navigateToEvent : LiveData<List<AtBase>>
        get() = _navigateToEvent

    private val _navigateToRunner = MutableLiveData<Boolean>()
    val navigateToRunner : LiveData<Boolean>
        get() = _navigateToRunner


    fun ball() {
        ballCount.value = ballCount.value!!.plus(1)
        if (ballCount.value == 4) {
            // single
            clearCount()
        }
    }

    fun strike() {
        strikeCount.value = strikeCount.value!!.plus(1)
        if (strikeCount.value == 3) {
            out()
        }
    }

    fun foul() {
        if (strikeCount.value!! < 2) {
            strikeCount.value = strikeCount.value!!.plus(1)
        }
    }

    fun out() {
        outCount.value = outCount.value!!.plus(1)

        if (outCount.value!! == 3) {
            // three out! switch
            outCount.value = 0
        } else {
            nextPlayer()
        }
    }

    fun safe() {
        atBaseList.clear()
        hitterEvent = Event(
                ball = ballCount.value ?: 0,
                strike = strikeCount.value ?: 0,
                out = outCount.value ?: 0,
                player = lineup[atBatNumber]
        )

        baseList[0] = lineup[atBatNumber]
        for ((index, player) in baseList.withIndex()) {
            if (player != null) {
                atBaseList.add(AtBase(base = index, player = player))
            }
        }

        _navigateToEvent.value = atBaseList
    }

    fun onEventNavigated() {
        _navigateToEvent.value = null
    }


    fun nextPlayer() {
        clearCount()
        // if current at bat is the last player of line up
        if (atBatNumber == (lineup.size-1)) {
            atBatNumber = 0
        } else {
            atBatNumber += 1
        }

        // change the hitter to next person in line
        baseList[0] = lineup[atBatNumber]
        atBatName.value = "第${atBatNumber+1}棒 ${baseList[0]!!.name}"

        // debugging
        for ((index, base) in baseList.withIndex()){
            Log.i("at base", "base $index is now ${base?.name}")
        }
        Log.i("at base", "--------------next-------------")
    }

    fun setNewBaseList(newList: Array<EventPlayer?>) {
        baseList = newList
    }

    fun clearCount() {
        ballCount.value = 0
        strikeCount.value = 0
    }

    fun clearBase() {
        baseList = arrayOf<EventPlayer?>(null, null, null, null)
    }

    fun switch() {
        outCount.value = 0 // ?
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("gillian", "Batting view model on clear")
    }
}