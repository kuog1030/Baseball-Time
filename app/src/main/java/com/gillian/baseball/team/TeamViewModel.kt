package com.gillian.baseball.team

import android.net.Uri
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

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()

    val teamPlayers: LiveData<MutableList<Player>>
        get() = _teamPlayers

    // team players without user itself (teammate only)
    private val _teammates = MutableLiveData<List<Player>>()

    val teammates: LiveData<List<Player>>
        get() = _teammates

    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog: LiveData<Boolean>
        get() = _showNewPlayerDialog

    private val _navigateToTeamStat = MutableLiveData<Boolean>()

    val navigateToTeamStat: LiveData<Boolean>
        get() = _navigateToTeamStat

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _editStatus = MutableLiveData<LoadStatus>()

    val editStatus: LiveData<LoadStatus>
        get() = _editStatus

    private val _newTeamImage = MutableLiveData<String>()

    val newTeamImage: LiveData<String>
        get() = _newTeamImage

    // For Team Stat
    private val _hitterStat = MutableLiveData<List<HitterBox>>()

    val hitterStat: LiveData<List<HitterBox>>
        get() = _hitterStat

    private val _pitcherStat = MutableLiveData<List<PitcherBox>>()

    val pitcherStat: LiveData<List<PitcherBox>>
        get() = _pitcherStat

    // For team info editing
    private val _editable = MutableLiveData<Boolean>(false)

    val editable : LiveData<Boolean>
        get() = _editable


    val teamName = MutableLiveData<String>()
    val teamAcronym = MutableLiveData<String>()
    val teamImage = MutableLiveData<String>()

    val photoToBeSent = MutableLiveData<Uri>()
    val rankList = MutableLiveData<List<Rank>>()

    fun startEdit() {
        if (_editable.value == true) {
            fetchTeamFromUserManager()
            photoToBeSent.value = null
        }
        _editable.value = !(_editable.value!!)
    }

    // team fragment on create -> init team page -> fetch team -> separate player & create rank list
    fun initTeamPage() {
        fetchTeamPlayer()
        fetchTeamFromUserManager()
    }

    
    private fun fetchTeamFromUserManager() {
        UserManager.team?.let {
            teamName.value = it.name
            teamImage.value = it.image
            teamAcronym.value = it.acronym
        }
    }

    
    private fun fetchTeamPlayer() {
        viewModelScope.launch {
            val result = repository.getTeamPlayer(UserManager.teamId)

            _teamPlayers.value = when (result) {
                is Result.Success -> {
                    val teamPlayer = result.data
                    separateTeamAndMe(teamPlayer, UserManager.userId)
                    createRankList(teamPlayer)
                    teamPlayer
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
            _refreshStatus.value = false
        }
    }


    private fun separateTeamAndMe(players: List<Player>, myId: String) {
        _teammates.value = players.filter { it.userId != myId }
        myself.value = players.firstOrNull { it.userId == myId }
    }

    // create rank list base on team players list
    fun createRankList(playerList: List<Player>) {
        rankList.value = playerList.toRankList()
    }


    init {
        initTeamPage()
    }

    // create the table view for Team Stat Page
    fun createStatTable(playerList: List<Player>) {
        val hitResult = mutableListOf(HitterBox())
        val pitchResult = mutableListOf(PitcherBox())

        for (player in playerList) {
            if (player.hitStat.atBat != 0) {
                hitResult.add(player.hitStat)
            }
            if (player.pitchStat.inningsPitched != 0) {
                pitchResult.add(player.pitchStat)
            }
        }

        _hitterStat.value = hitResult
        _pitcherStat.value = pitchResult
    }

    // updating team info process :
    // check if info filled -> (upload photo) -> update team info
    fun checkIfInfoFilled() {
        if (teamAcronym.value != "" && teamName.value != "") {
            _editStatus.value = LoadStatus.LOADING

            // check if uploading new photo needed
            if (photoToBeSent.value != null) {
                uploadPhoto(photoToBeSent.value!!)
            } else {
                _newTeamImage.value = teamImage.value
            }
        }
    }


    private fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            val result = repository.uploadImage(uri)
            _newTeamImage.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    _editStatus.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }


    fun updateTeamInfo(imageUrl: String) {
        val newTeam = Team(name = teamName.value ?: "",
                            acronym = teamAcronym.value ?: "",
                            image = imageUrl,
                            id = UserManager.teamId)

        viewModelScope.launch {
            when (repository.updateTeamInfo(newTeam)) {
                is Result.Success -> {
                    UserManager.team = newTeam
                    photoToBeSent.value = null
                    teamImage.value = imageUrl
                    _editStatus.value = LoadStatus.DONE
                    startEdit()
                }
                else -> {
                    _editStatus.value = LoadStatus.ERROR
                    startEdit()
                }
            }
        }
    }


    fun navigateToTeamStat() {
        teamPlayers.value?.let {
            createStatTable(it)
            _navigateToTeamStat.value = true
        }
    }


    fun onTeamStatNavigated() {
        _navigateToTeamStat.value = null
    }


    fun navigateToNewPlayer() {
        _showNewPlayerDialog.value = true
    }


    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }


    fun refresh() {
        fetchTeamPlayer()
    }

}