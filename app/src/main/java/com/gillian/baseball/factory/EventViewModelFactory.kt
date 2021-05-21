package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.data.EventInfo
import com.gillian.baseball.game.dialog.EventDialogViewModel

class EventViewModelFactory constructor(
        private val repository: BaseballRepository,
        private var eventInfo: EventInfo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(EventDialogViewModel::class.java) ->
                    EventDialogViewModel(repository, eventInfo)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}