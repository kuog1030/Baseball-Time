package com.gillian.baseball.game.dialog

import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.source.BaseballRepository

class HitterViewModel(private val repository: BaseballRepository) : ViewModel() {

    fun single() {}

    fun Double() {}

    fun triple() {}

    fun homerun() {}

    fun hbp() {}

    fun error() {}

    fun droppedThird(){}

}