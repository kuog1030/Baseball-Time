package com.gillian.baseball.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: BaseballRepository) : ViewModel() {

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()

    val teamPlayers: LiveData<MutableList<Player>>
        get() = _teamPlayers

    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog: LiveData<Boolean>
        get() = _showNewPlayerDialog

    var team = MutableLiveData<Team>()
    var teamName = MutableLiveData<String>()

    fun getTeam() {
        viewModelScope.launch {
            team = repository.getTeam(UserManager.teamId)
            repository.getHittersStat("mLJAsjiRThY5TgjRInpx", "")
        }
    }


    fun getTeamPlayer() {

        viewModelScope.launch {

            val result = repository.getTeamPlayer(UserManager.teamId)

            _teamPlayers.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                is Result.Fail -> {
                    null
                }
                is Result.Error -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
    }


    init {
        getTeamPlayer()
        if (UserManager.teamName == null) {
            getTeam()
        } else {
            teamName.value = UserManager.teamName
        }
    }


    fun addNewPlayer() {
        _showNewPlayerDialog.value = true
    }

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        getTeamPlayer()
    }


}