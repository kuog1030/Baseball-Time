package com.gillian.baseball.game.finalGame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.source.BaseballRepository

class FinalViewModel(private val repository: BaseballRepository, private val myGame: MyGame) : ViewModel() {

    var score = myGame.game.box.score

    var run = myGame.game.box.run
    var hit = myGame.game.box.hit
    var error = myGame.game.box.error

}