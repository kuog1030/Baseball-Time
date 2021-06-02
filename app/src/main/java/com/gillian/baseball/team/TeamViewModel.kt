package com.gillian.baseball.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: BaseballRepository) : ViewModel() {


    val customBaseInt = MutableLiveData(101)

    val myself = MutableLiveData<Player>()
    val myAvg = MutableLiveData<String>()
    val myObp = MutableLiveData<String>()
    val mySlg = MutableLiveData<String>()

    val initUser = MutableLiveData<Team>()

    val testAllEvent = MutableLiveData<List<Event>>()

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()

    val teamPlayers: LiveData<MutableList<Player>>
        get() = _teamPlayers

    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog: LiveData<Boolean>
        get() = _showNewPlayerDialog

    private val _statusMe = MutableLiveData<LoadStatus>()

    val statusMe: LiveData<LoadStatus>
        get() = _statusMe

    private val _statusTeam = MutableLiveData<LoadStatus>()

    val statusTeam: LiveData<LoadStatus>
        get() = _statusTeam


    var teamName = MutableLiveData<String>()


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
        // TODO() get team info
        if (UserManager.team == null || UserManager.teamId == "") {
            initMyUserData()
        } else {
            initTeamPage()
        }
    }

    fun initMyUserData() {
        viewModelScope.launch {
            val result = repository.getTeam(UserManager.teamId)
            initUser.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun initTeamPage() {
        getMyPlayerInfo()
        getTeamPlayer()
        teamName.value = UserManager.teamName
    }


    fun getMyPlayerInfo() {
        viewModelScope.launch {
            _statusMe.value = LoadStatus.LOADING
            val myResult = repository.getOnePlayer(UserManager.playerId)
            myself.value = when (myResult) {
                is Result.Success -> {
                    _statusMe.value = LoadStatus.DONE
                    myResult.data
                }
                is Result.Fail -> {
                    _statusMe.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _statusMe.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _statusMe.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun addNewPlayer() {
        _showNewPlayerDialog.value = true
    }

    fun updateMyStatUi() {
        val statFormat = "%.3f"
        myself.value?.let {
            myAvg.value = statFormat.format(it.hitStat.myAverage() ?: 0F)
            myObp.value = statFormat.format(it.hitStat.myObp() ?: 0F)
            mySlg.value = statFormat.format(it.hitStat.mySlg() ?: 0F)
        }
    }



    fun navigate() {
        Log.i("gillian", " onclick yes!")
    }

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        getTeamPlayer()
    }


}