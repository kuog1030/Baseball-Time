package com.gillian.baseball.team

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.ext.toRankList
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: BaseballRepository) : ViewModel() {


    val myself = MutableLiveData<Player>()
    val myAvg = MutableLiveData<String>()
    val myObp = MutableLiveData<String>()
    val mySlg = MutableLiveData<String>()

    val initUser = MutableLiveData<Team>()

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
    val teamAcronym = MutableLiveData<String>()
    val teamImage = MutableLiveData<String>()


    val editable = MutableLiveData<Boolean>(false)
    val readyToSentPhoto = MutableLiveData<Uri>()
    val rankList = MutableLiveData<List<Rank>>()

    fun startEdit() {
        editable.value = !(editable.value!!)
    }

    fun createRankList(playerList: List<Player>) {
        rankList.value = playerList.toRankList()
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
        if (UserManager.team == null) {
            Log.i("gillian67", "team null?")
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
        fetchMyPlayerInfo()
        getTeamPlayer()
        teamName.value = UserManager.team?.name
        teamImage.value = UserManager.team?.image
        teamAcronym.value = UserManager.team?.acronym
    }


    fun fetchMyPlayerInfo() {
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

    fun updateTeamInfo() {
        // 6/8 TODO
//        if (teamAcronym.value != "" && teamName.value != "") {
//
//            if (readyToSentPhoto.value != null) {
//
//            }
//        }
    }


    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        getTeamPlayer()
    }


}