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

class BattingViewModel(private val repository: BaseballRepository, var lineup: List<EventPlayer>) : ViewModel() {

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

//    var lineup = mutableListOf<EventPlayer>(EventPlayer("0024", "1陳傑憲", "24"),
//            EventPlayer("0032","2蘇智傑", "32"),
//            EventPlayer("0013", "3陳鏞基", "13"),
//            EventPlayer("0077", "4林安可", "77"),
//            EventPlayer("0065", "5陳重羽", "65"),
//            EventPlayer("0064", "6林靖凱", "64"),
//            EventPlayer("0052", "7張偉聖", "52"),
//            EventPlayer("0031", "8林岱安", "31"),
//            EventPlayer("0039", "9林祖傑", "39")
//    )


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

    private val _navigateToOnBase = MutableLiveData<Int>()
    val navigateToOnBase : LiveData<Int>
        get() = _navigateToOnBase

    private val _navigateToOut = MutableLiveData<List<AtBase>>()
    val navigateToOut : LiveData<List<AtBase>>
        get() = _navigateToOut


    var firstBaseVisible = MutableLiveData<Boolean>(false)
    var firstBaseName = MutableLiveData<String>("")
    var secondBaseVisible = MutableLiveData<Boolean>(false)
    var secondBaseName = MutableLiveData<String>("")
    var thirdBaseVisible = MutableLiveData<Boolean>(false)
    var thirdBaseName = MutableLiveData<String>("")


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

        if (strikeCount.value == 3) {
            sendEvent(Event(player = lineup[atBatNumber],
                    result = 9,
                    ball = ballCount.value ?: 0,
                    strike = totalStrike,
                    out = 1))
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

    fun onBaseOut(base: Int) {
        outCount.value = outCount.value!!.plus(1)
        if (outCount.value!! == 3) {
            // three out! switch
            outCount.value = 0
            Log.i("at base", "*-------------change!------------*")
        } else {
            baseList[base] = null
        }
    }

    fun toEventDialog(isSafe: Boolean) {
        atBaseList.clear()
        hitterEvent = Event(
                ball = ballCount.value ?: 0,
                strike = totalStrike,
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
            if (outCount.value == 2) {
                _navigateToOut.value = listOf(AtBase(0, baseList[0]!!))
            } else {
                _navigateToOut.value = atBaseList
            }
        }
    }

    fun showOnBaseDialog(base: Int) {
        _navigateToOnBase.value = base
    }

    fun onEventNavigated() {
        _navigateToEvent.value = null
    }

    fun onOutNavigated() {
        _navigateToOut.value = null
    }

    fun onOnBaseNavigated() {
        _navigateToOnBase.value = null
    }


    fun advanceBase(myself: Int) {
        var end = myself
        while ( baseList[end] != null) {
            if (end == 3) {
                // 擠壘事件造成的得分
                sendEvent(Event(player = baseList[3]!!, run = 1))
                break
            }
            end += 1
        }
        for ( i in end downTo (myself+1)) {
            baseList[i] = baseList[i-1]
        }
        baseList[myself] = null

//        // debugging
//        Log.i("at base ", "--------------壘上事件變化啦-------------")
//        for ((index, base) in baseList.withIndex()){
//            Log.i("at base advance", "base $index is now ${base?.name}")
//        }
//        Log.i("at base ", "--------------壘上事件結束-------------")
        updateRunnerUI() // 放這邊合理嗎?
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
        updateRunnerUI()


        // debugging
        for ((index, base) in baseList.withIndex()){
            Log.i("at base next player()", "base $index is now ${base?.name}")
        }
        Log.i("at base ", "--------------next-------------")
    }

    fun updateRunnerUI() {
        if (baseList[1] == null) {
            firstBaseVisible.value = false
        } else {
            firstBaseVisible.value = true
            firstBaseName.value = baseList[1]!!.name
        }
        if (baseList[2] == null) {
            secondBaseVisible.value = false
        } else {
            secondBaseVisible.value = true
            secondBaseName.value = baseList[2]!!.name
        }

        if (baseList[3] == null) {
            thirdBaseVisible.value = false
        } else {
            thirdBaseVisible.value = true
            thirdBaseName.value = baseList[3]!!.name
        }
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
        clearCount()
        outCount.value = 0 // ?
        // clearBase?
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("gillian", "Batting view model on clear")
    }
}