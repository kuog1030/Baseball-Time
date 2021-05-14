package com.gillian.baseball.game.batting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class BattingViewModel(private val repository: BaseballRepository) : ViewModel() {

    var ballCount = MutableLiveData<Int>().apply {
        value = 0
    }
    var strikeCount = MutableLiveData<Int>().apply{
        value = 0
    }
    var totalStrike = 0
    var outCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val lineup = mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"),
            EventPlayer("0032","蘇智傑", "32"),
            EventPlayer("0013", "陳鏞基", "13"),
            EventPlayer("0077", "林安可", "77"),
            EventPlayer("0065", "陳重羽", "65"),
            EventPlayer("0064", "林靖凱", "64"),
            EventPlayer("0052", "張偉聖", "52"),
            EventPlayer("0031", "林岱安", "31"),
            EventPlayer("0039", "林祖傑", "39")
    )


    // 進到dialog的時候帶過去的用球數
    lateinit var hitterEvent : Event

    // 一般來說在這裡都是使用base list
    var baseList = arrayOf<EventPlayer?>(lineup[0], null, null, null)
    // 要進到dialog的樣子
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

    private val _navigateToOut = MutableLiveData<List<AtBase>>()
    val navigateToOut : LiveData<List<AtBase>>
        get() = _navigateToOut


    fun ball() {
        ballCount.value = ballCount.value!!.plus(1)
        if (ballCount.value == 4) {
            // single
            ballFour()
        }
    }

    fun strike() {
        strikeCount.value = strikeCount.value!!.plus(1)
        totalStrike += 1

        // TODO()
        if (strikeCount.value == 3) {
            sendEvent(Event(player = lineup[atBatNumber],
                    result = 9,
                    ball = ballCount.value ?: 0,
                    strike = totalStrike,
                    out = outCount.value ?: 0))
            out()
        }
    }

    fun foul() {
        if (strikeCount.value!! < 2) {
            strikeCount.value = strikeCount.value!!.plus(1)
        }
        totalStrike += 1
    }

    fun out() {
        outCount.value = outCount.value!!.plus(1)
        if (outCount.value!! == 3) {
            // three out! switch
            outCount.value = 0
            Log.i("at base", "*-------------change!------------*")
        } else {
            nextPlayer()
        }
    }

    fun toEventDialog(isSafe: Boolean) {
        atBaseList.clear()
        hitterEvent = Event(
                ball = ballCount.value ?: 0,
                strike = totalStrike,
                out = outCount.value ?: 0,
                player = lineup[atBatNumber]
        )

        baseList[0] = lineup[atBatNumber]
        for ((index, player) in baseList.withIndex()) {
            if (player != null) {
                atBaseList.add(AtBase(base = index, player = player))
            }
        }

        if (isSafe) {
            _navigateToEvent.value = atBaseList
        } else {
            //TODO()應該何時加上出局數呢? 如果只是不小心按到話??
            if (outCount.value == 2) {
                _navigateToOut.value = listOf(AtBase(0, baseList[0]!!))
            } else {
                _navigateToOut.value = atBaseList
            }
        }
    }

    fun onEventNavigated() {
        _navigateToEvent.value = null
    }

    fun onOutNavigated() {
        _navigateToOut.value = null
    }

    fun advanceBase(start_: Int) {
        var start = start_
        while ( baseList[start] != null) {
            if (start == 3) {
                sendEvent(Event(player = baseList[3]!!, run = 1))
                break
            }
            start += 1
        }
        for ( i in start downTo 1) {
            baseList[i] = baseList[i-1]
        }
    }

    fun ballFour() {
        sendEvent(Event(player = lineup[atBatNumber],
                result = 8,
                ball = 4,
                strike = totalStrike,
                out = outCount.value ?: 0))
        advanceBase(0)
        clearCount()
        nextPlayer()
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            repository.sendEvent(event)
        }
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
            Log.i("at base next player()", "base $index is now ${base?.name}")
        }
        Log.i("at base ", "--------------next-------------")
    }

    fun setNewBaseList(newList: Array<EventPlayer?>) {
        baseList = newList
    }

    fun clearCount() {
        totalStrike = 0
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