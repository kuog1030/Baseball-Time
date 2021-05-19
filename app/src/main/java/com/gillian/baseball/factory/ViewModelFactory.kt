package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.firstlogin.FirstLoginViewModel
import com.gillian.baseball.game.order.OrderViewModel
import com.gillian.baseball.game.dialog.EventDialogViewModel
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.team.newplayer.NewPlayerViewModel

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
                isAssignableFrom(FirstLoginViewModel::class.java) ->
                    FirstLoginViewModel(repository)
                isAssignableFrom(TeamViewModel::class.java) ->
                    TeamViewModel(repository)
                isAssignableFrom(NewPlayerViewModel::class.java) ->
                    NewPlayerViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}