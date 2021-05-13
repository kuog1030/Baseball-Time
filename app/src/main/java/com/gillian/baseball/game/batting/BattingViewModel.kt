package com.gillian.baseball.game.batting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository

class BattingViewModel(private val repository: BaseballRepository?=null) : ViewModel() {

    var ballCount = MutableLiveData<Int>().apply {
        value = 0
    }
    var strikeCount = MutableLiveData<Int>().apply{
        value = 0
    }
    var outCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val lineup = mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"), EventPlayer("0032","蘇智傑", "32"))
    var atBatNumber = 0

    private val _navigateToHitter = MutableLiveData<Event>()
    val navigateToHitter : LiveData<Event>
        get() = _navigateToHitter

    private val _navigateToRunner = MutableLiveData<Boolean>()
    val navigateToRunner : LiveData<Boolean>
        get() = _navigateToRunner


    fun ball() {
        if (ballCount.value == 3) {
            // single
            clearCount()
        } else {
            ballCount.value = ballCount.value!!.plus(1)
        }
    }

    fun strike() {
        if (strikeCount.value == 2) {
            out()
        } else {
            strikeCount.value = strikeCount.value!!.plus(1)
        }
    }

    fun foul() {
        if (strikeCount.value!! < 2) {
            strikeCount.value = strikeCount.value!!.plus(1)
        }
    }

    fun out() {
        if (outCount.value!! == 2) {
            // switch
            outCount.value = 0
        } else {
            outCount.value = outCount.value!!.plus(1)
        }
        clearCount()
    }

    fun safe() {
        Log.i("Gillian", "ready to navigate to 上壘 and at bat number is $atBatNumber")
        _navigateToHitter.value = Event(
            ball = ballCount.value ?: 0,
            strike = strikeCount.value ?: 0,
            out = outCount.value ?: 0,
            player = lineup[atBatNumber]
        )
    }

    fun onHitterNavigated() {
        _navigateToHitter.value = null
    }


    fun nextPlayer() {
        // if current at bat is the last player of line up
        if (atBatNumber == (lineup.size-1)) {
            atBatNumber = 0
        } else {
            atBatNumber += 1
        }
    }

    fun clearCount() {
        ballCount.value = 0
        strikeCount.value = 0
    }

    fun switch() {
        outCount.value = 0 // ?
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("gillian", "Batting view model on clear")
    }
}