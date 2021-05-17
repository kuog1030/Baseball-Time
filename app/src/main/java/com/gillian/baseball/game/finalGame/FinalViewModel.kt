package com.gillian.baseball.game.finalGame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballRepository

class FinalViewModel(private val repository: BaseballRepository, private val argsGame: Game) : ViewModel() {

    val scoreBox = MutableLiveData<String>()
    var scoreText = ""
    var runText = ""
    var hitText = ""
    var errorText = ""


    val runBox = MutableLiveData<String>()
    val hitBox = MutableLiveData<String>()
    val errorBox = MutableLiveData<String>()

    init {

        Log.i("gillian", "score box is ${argsGame.box.score}")

        for (score in argsGame.box.score) {
            scoreText += "$score - "
        }

        for (i in 0..1) {
            runText += "${argsGame.box.run[i]} -"
            hitText += "${argsGame.box.hit[i]} -"
            errorText += "${argsGame.box.error[i]} -"
        }

        scoreBox.value = scoreText
        runBox.value = runText
        hitBox.value = hitText
        errorBox.value = errorText
    }
}