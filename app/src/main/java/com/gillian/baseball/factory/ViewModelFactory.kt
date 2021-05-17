package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.order.OrderViewModel
import com.gillian.baseball.game.dialog.EventDialogViewModel

class ViewModelFactory constructor(
    private val repository: BaseballRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(EventDialogViewModel::class.java) ->
                    EventDialogViewModel(repository)
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}