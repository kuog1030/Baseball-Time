package com.gillian.baseball.game.batting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.source.BaseballRepository

class BattingViewModel(private val baseballRepository: BaseballRepository) : ViewModel() {

    // 是不是要傳game的class進來


    var ballCount = 0
    var strikeCount = 0
    var outCount = 0


    private val _navigateToHitter = MutableLiveData<Boolean>()
    val navigateToHitter : LiveData<Boolean>
        get() = _navigateToHitter

    private val _navigateToRunner = MutableLiveData<Boolean>()
    val navigateToRunner : LiveData<Boolean>
        get() = _navigateToRunner


    fun ball() {}

    fun strike() {}

    fun foul() {}

    fun out() {}

    fun safe() {}


    fun nextPlayer() {}

}