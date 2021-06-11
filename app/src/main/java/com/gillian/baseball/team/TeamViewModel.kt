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
    val initUser = MutableLiveData<Team>()

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()

    val teamPlayers: LiveData<MutableList<Player>>
        get() = _teamPlayers

    private val _teamPlayersWithMe = MutableLiveData<List<Player>>()

    val teamPlayersWithMe: LiveData<List<Player>>
        get() = _teamPlayersWithMe

    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog: LiveData<Boolean>
        get() = _showNewPlayerDialog

    private val _navigateToTeamStat = MutableLiveData<Boolean>()

    val navigateToTeamStat: LiveData<Boolean>
        get() = _navigateToTeamStat

    private val _statusMe = MutableLiveData<LoadStatus>()

    val statusMe: LiveData<LoadStatus>
        get() = _statusMe

    private val _statusEdit = MutableLiveData<LoadStatus>()

    val statusEdit: LiveData<LoadStatus>
        get() = _statusEdit

    private val _newTeamImage = MutableLiveData<String>()

    val newTeamImage : LiveData<String>
        get() = _newTeamImage

    // For Team Stat
    private val _hitterStat = MutableLiveData<List<HitterBox>>()

    val hitterStat: LiveData<List<HitterBox>>
        get() = _hitterStat

    private val _pitcherStat = MutableLiveData<List<PitcherBox>>()

    val pitcherStat: LiveData<List<PitcherBox>>
        get() = _pitcherStat


    var teamName = MutableLiveData<String>()
    val teamAcronym = MutableLiveData<String>()
    val teamImage = MutableLiveData<String>()


    val editable = MutableLiveData<Boolean>(false)
    val readyToSentPhoto = MutableLiveData<Uri>()
    val rankList = MutableLiveData<List<Rank>>()

    fun startEdit() {
        if (editable.value == true) {
            teamName.value = UserManager.team?.name
            teamAcronym.value = UserManager.team?.acronym
            teamImage.value = UserManager.team?.image
            readyToSentPhoto.value = null
        }
        editable.value = !(editable.value!!)
    }

    fun createRankList(playerList: List<Player>) {
        rankList.value = playerList.toRankList()
    }


    fun fetchTeamPlayer() {

        viewModelScope.launch {

            val result = repository.getTeamPlayer(UserManager.teamId)

            _teamPlayers.value = when (result) {
                is Result.Success -> {
                    _teamPlayersWithMe.value = result.data.filter { it.userId != UserManager.userId }
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
        fetchTeamPlayer()
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


    fun checkIfInfoFilled() {
        if (teamAcronym.value != "" && teamName.value != "") {
            _statusEdit.value = LoadStatus.LOADING
            if (readyToSentPhoto.value != null) {
                uploadPhoto(readyToSentPhoto.value!!)
            } else {
                _newTeamImage.value = teamImage.value
            }
        }
    }

    fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            val result = repository.uploadImage(uri)
            _newTeamImage.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    _statusEdit.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }



    fun updateTeamInfo(imageUrl: String) {
        // 6/8 TODO
        val newTeam = Team(name = teamName.value ?: "", acronym = teamAcronym.value ?: "", image = imageUrl, id = UserManager.teamId)
        viewModelScope.launch {

            val result = repository.updateTeamInfo(newTeam)
            when (result) {
                is Result.Success -> {
                    UserManager.team = newTeam
                    readyToSentPhoto.value = null
                    teamImage.value = imageUrl
                    _statusEdit.value = LoadStatus.DONE
                    startEdit()
                }
                else -> {
                    _statusEdit.value = LoadStatus.ERROR
                    startEdit()
                }
            }

        }
    }

    // For Tea, Stat
    fun createStatTable(playerList: List<Player>){
        val hitResult = mutableListOf(HitterBox())
        val pitchResult = mutableListOf(PitcherBox())

        for (player in playerList) {
            hitResult.add(player.hitStat)
            if (player.pitchStat.inningsPitched != 0) {
                pitchResult.add(player.pitchStat)
            }
        }

        _hitterStat.value = hitResult
        _pitcherStat.value = pitchResult
    }


    fun navigateToTeamStat() {
        teamPlayers.value?.let{
            createStatTable(it)
            _navigateToTeamStat.value = true
        }
    }

    fun onTeamStatNavigated() {
        _navigateToTeamStat.value = null
    }


    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        fetchTeamPlayer()
    }


}