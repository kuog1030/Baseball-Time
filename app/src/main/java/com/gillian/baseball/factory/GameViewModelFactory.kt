package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.GameViewModel

class GameViewModelFactory constructor(
    private val repository: BaseballRepository,
    private val game: Game?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel(repository, game)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}