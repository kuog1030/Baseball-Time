package com.gillian.baseball.loginfirst

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
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch


class LoginFirstViewModel(private val repository: BaseballRepository, private val user: User) : ViewModel() {

    val searchPlayerId = MutableLiveData<String>()

    val playerList = MutableLiveData<List<Player>>()

    var player = MutableLiveData<Player>()

    val newTeamName = MutableLiveData<String>()

    val newPlayerName = MutableLiveData<String>()

    val newPlayerNumber = MutableLiveData<String>()

    val registerInfo = MutableLiveData<Boolean>(false)

    private val _signUpUserFromRegister = MutableLiveData<User>()

    val signUpUserFromRegister: LiveData<User>
        get() = _signUpUserFromRegister

    private val _signUpUser = MutableLiveData<User>()

    val signUpUser: LiveData<User>
        get() = _signUpUser

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _proceedFetchTeam = MutableLiveData<Boolean>()

    val proceedFetchTeam: LiveData<Boolean>
        get() = _proceedFetchTeam

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam

    private val _navigateFromRegister = MutableLiveData<Team>()

    val navigateFromRegister: LiveData<Team>
        get() = _navigateFromRegister


    // two way to sign up 1. new create 2. register existed player


    //                             (team, player id)  (user id)
    // Register: searchPlayer -> signUpUserFromRegister -> registerPlayer -> fetchTeam
    fun searchPlayer() {
        registerInfo.value = false
        if (searchPlayerId.value.isNullOrEmpty()) {
            playerList.value = emptyList()
            player.value = null
        } else {
            viewModelScope.launch {
                val result = repository.searchPlayer(searchPlayerId.value!!)
                playerList.value = when(result) {
                    is Result.Success -> {
                        _errorMessage.value = if (result.data.isEmpty()) (getString(R.string.error_no_player_result)) else null
                        result.data
                    }
                    is Result.Fail -> {
                        _errorMessage.value = result.error
                        null
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.exception.toString()
                        null
                    }
                    else -> {
                        _errorMessage.value = Util.getString(R.string.return_nothing)
                        null
                    }
                }
            }
        }
    }

    fun signUpUserFromRegister() {
        registerInfo.value = false
        // extract info from existed player
        player.value?.let{
            user.playerId = it.id
            user.teamId = it.teamId
            UserManager.playerId = it.id
            UserManager.teamId = it.teamId

            viewModelScope.launch {
                _status.value = LoadStatus.LOADING
                val result = repository.signUpUser(user)
                _signUpUserFromRegister.value = when (result) {
                    is Result.Success -> {
                        _errorMessage.value = null
                        result.data
                    }
                    is Result.Fail -> {
                        _errorMessage.value = result.error
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.exception.toString()
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    else -> {
                        _errorMessage.value = Util.getString(R.string.return_nothing)
                        _status.value = LoadStatus.ERROR
                        null
                    }
                }
            }
        }
    }

    fun registerPlayer() {
        player.value?.let {
            viewModelScope.launch {
                val result = repository.registerPlayer(it.id)

                _proceedFetchTeam.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    is Result.Fail -> {
                        _errorMessage.value = result.error
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.exception.toString()
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    else -> {
                        _errorMessage.value = Util.getString(R.string.return_nothing)
                        _status.value = LoadStatus.ERROR
                        null
                    }
                }
            }
        }
    }

    fun fetchTeam() {
        viewModelScope.launch {
            player.value?.let{
                val result = repository.getTeam(it.teamId)
                _navigateFromRegister.value = when (result) {
                    is Result.Success -> {
                        _errorMessage.value = null
                        _status.value = LoadStatus.DONE
                        result.data
                    }
                    is Result.Fail -> {
                        _errorMessage.value = result.error
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.exception.toString()
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    else -> {
                        _errorMessage.value = getString(R.string.return_nothing)
                        _status.value = LoadStatus.ERROR
                        null
                    }
                }
            }
        }
    }


    //          (user id)  (team, team id, player id)
    // Create: signUpUser -> initTeamAndPlayer
    private fun allInfoFilled() : Boolean {
        return if (newTeamName.value.isNullOrEmpty() || newPlayerNumber.value.isNullOrEmpty() || newPlayerName.value == null) {
            _errorMessage.value = getString(R.string.error_init_team_player)
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    fun signUpUser() {
        if (allInfoFilled()) {
            viewModelScope.launch {
                _status.value = LoadStatus.LOADING
                val result = repository.signUpUser(user)
                _signUpUser.value = when (result) {
                    is Result.Success -> {
                        _errorMessage.value = null
                        result.data
                    }
                    is Result.Fail -> {
                        _errorMessage.value = result.error
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.exception.toString()
                        _status.value = LoadStatus.ERROR
                        null
                    }
                    else -> {
                        _errorMessage.value = getString(R.string.return_nothing)
                        _status.value = LoadStatus.ERROR
                        null
                    }
                }
            }
        }
    }

    fun initTeamAndPlayer() {
        val numberInt = newPlayerNumber.value!!.toInt()

        val team = Team(name = newTeamName.value ?: "")
        val player = Player(name = newPlayerName.value ?: "", number = numberInt, image = user.image)

        viewModelScope.launch {
            // UserManager.playerId & teamId will be set
            val result = repository.initTeamAndPlayer(team, player)
            _navigateToTeam.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    _status.value = LoadStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

    fun fromRegisterNavigated() {
        _navigateFromRegister.value = null
    }

    fun toggleRegisterInfo() {
        registerInfo.value = !(registerInfo.value!!)
    }

}