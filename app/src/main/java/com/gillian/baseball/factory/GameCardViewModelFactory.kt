package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.order.OrderViewModel

class GameCardViewModelFactory constructor(
        private val repository: BaseballRepository,
        private val gameCard: GameCard?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(OrderViewModel::class.java) ->
                        OrderViewModel(repository, gameCard)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}