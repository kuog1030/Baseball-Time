package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.allbroadcast.AllBroadcastViewModel
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.broadcast.BroadcastViewModel
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.editplayer.EditPlayerViewModel
import com.gillian.baseball.firstlogin.FirstLoginViewModel
import com.gillian.baseball.login.LoginViewModel
import com.gillian.baseball.order.OrderViewModel
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.newplayer.NewPlayerViewModel
import com.gillian.baseball.statistics.statgame.StatGameViewModel
import com.gillian.baseball.statistics.statplayer.StatPlayerViewModel

class ViewModelFactory constructor(
    private val repository: BaseballRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(FirstLoginViewModel::class.java) ->
                    FirstLoginViewModel(repository)
                isAssignableFrom(TeamViewModel::class.java) ->
                    TeamViewModel(repository)
                isAssignableFrom(NewPlayerViewModel::class.java) ->
                    NewPlayerViewModel(repository)
                isAssignableFrom(AllGamesViewModel::class.java) ->
                    AllGamesViewModel(repository)
                isAssignableFrom(StatGameViewModel::class.java) ->
                    StatGameViewModel(repository)
                isAssignableFrom(StatPlayerViewModel::class.java) ->
                    StatPlayerViewModel(repository)
                isAssignableFrom(EditPlayerViewModel::class.java)->
                    EditPlayerViewModel(repository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)
                isAssignableFrom(BroadcastViewModel::class.java) ->
                    BroadcastViewModel(repository)
                isAssignableFrom(AllBroadcastViewModel::class.java) ->
                    AllBroadcastViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}