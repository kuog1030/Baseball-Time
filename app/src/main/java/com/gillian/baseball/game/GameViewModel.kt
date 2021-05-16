package com.gillian.baseball.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballRepository

class GameViewModel(private val repository: BaseballRepository, private val argument: Game?) : ViewModel() {

    private val _game = MutableLiveData<Game>(argument)

    val game: LiveData<Game>
        get() = _game

    var homeRun = MutableLiveData<Int>(0)
    var guestRun = MutableLiveData<Int>(0)


    fun homeScored() {
        homeRun.value = homeRun.value!!.plus(1)

    }

    fun guestScored() {
        guestRun.value = guestRun.value!!.plus(1)
    }


}