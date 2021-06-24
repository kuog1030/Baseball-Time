package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.OnBaseInfo
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.onbase.OnBaseViewModel


class OnBaseViewModelFactory constructor(
        private val repository: BaseballRepository,
        private var onBaseInfo: OnBaseInfo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(OnBaseViewModel::class.java) ->
                        OnBaseViewModel(repository, onBaseInfo)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}