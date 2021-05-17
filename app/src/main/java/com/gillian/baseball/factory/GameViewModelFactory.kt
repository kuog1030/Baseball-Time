package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.GameViewModel
import com.gillian.baseball.game.finalGame.FinalViewModel

class GameViewModelFactory constructor(
    private val repository: BaseballRepository,
    private val myGame: MyGame
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel(repository, myGame)
                isAssignableFrom(FinalViewModel::class.java) ->
                    FinalViewModel(repository, myGame)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}