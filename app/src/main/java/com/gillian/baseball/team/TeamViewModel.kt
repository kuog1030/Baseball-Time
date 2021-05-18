package com.gillian.baseball.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: BaseballRepository) : ViewModel() {

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()

    val teamPlayers : LiveData<MutableList<Player>>
        get() = _teamPlayers


    fun getTeamPlayer() {
        viewModelScope.launch {
            _teamPlayers.value = repository.getTeamPlayer("teamId")
        }
    }


    init {
        getTeamPlayer()
    }


}