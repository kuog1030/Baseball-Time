package com.gillian.baseball.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballRepository

//class GameViewModel : ViewModel() {
//
//    private val _game = MutableLiveData<Game>()
//    val game: LiveData<Game>
//        get() = _game
//
//    fun initGame(args: Game) {
//        _game.value = args
//    }
//
//}

class GameViewModel(private val repository: BaseballRepository, private val argument: Game?) : ViewModel() {

    private val _game = MutableLiveData<Game>().apply{
        argument?.let{
            value = it
        }
    }

    val game: LiveData<Game>
        get() = _game

}