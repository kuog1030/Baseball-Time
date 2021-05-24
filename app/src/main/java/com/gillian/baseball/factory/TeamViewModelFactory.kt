package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.newgame.NewGameViewModel

class TeamViewModelFactory constructor(
    private val repository: BaseballRepository,
    private val team: Team
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NewGameViewModel::class.java) ->
                    NewGameViewModel(repository, team)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}