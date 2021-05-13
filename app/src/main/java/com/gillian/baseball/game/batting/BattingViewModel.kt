package com.gillian.baseball.game.batting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


    private val _navigateToHitter = MutableLiveData<Boolean>()
    val navigateToHitter : LiveData<Boolean>
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
        _navigateToHitter.value = true
    }

    fun onHitterNavigated() {
        _navigateToHitter.value = null
    }


    fun nextPlayer() {

    }

    fun clearCount() {
        ballCount.value = 0
        strikeCount.value = 0
    }

    fun switch() {
        outCount.value = 0 // ?
    }

}