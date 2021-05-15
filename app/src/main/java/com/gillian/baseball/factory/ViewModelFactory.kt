package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.game.batting.BattingViewModel
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.dialog.EventDialogViewModel
import com.gillian.baseball.game.dialog.OnBaseDialogViewModel

class ViewModelFactory constructor(
    private val repository: BaseballRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(BattingViewModel::class.java) ->
                    BattingViewModel(repository)
                isAssignableFrom(EventDialogViewModel::class.java) ->
                    EventDialogViewModel(repository)
//                isAssignableFrom(HitterViewModel::class.java) ->
//                    HitterViewModel(repository)
                isAssignableFrom(OnBaseDialogViewModel::class.java) ->
                    OnBaseDialogViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}