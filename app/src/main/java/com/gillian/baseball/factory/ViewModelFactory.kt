package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.game.batting.BattingViewModel
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.dialog.HitterViewModel

class ViewModelFactory constructor(
    private val repository: BaseballRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(BattingViewModel::class.java) ->
                    BattingViewModel(repository)
                isAssignableFrom(HitterViewModel::class.java) ->
                    HitterViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}