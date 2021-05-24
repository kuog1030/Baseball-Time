package com.gillian.baseball.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.HitterBox
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


    //TODO() 測試用 記得刪掉
    val oneGameHitterStat = MutableLiveData<List<HitterBox>>()


    var team = MutableLiveData<Team>()
    var teamName = MutableLiveData<String>()

    fun getTeam() {
        viewModelScope.launch {
            team = repository.getTeam(UserManager.teamId)
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

    // TODO()測試用記得刪
    fun getHitterStat() {
        viewModelScope.launch {
            val result = repository.getHittersStat("pP4lAdxbYXDHgiAhRlnI", "")
            oneGameHitterStat.value = when (result) {
                is Result.Success -> {
                    Log.i("gillian", "success")
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
        getHitterStat()  //TODO() 測試用記得刪
        teamName.value = UserManager.teamName

    }


    fun addNewPlayer() {
        _showNewPlayerDialog.value = true
    }

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        Log.i("gillian", "team view model refresh")
        getTeamPlayer()
    }


}