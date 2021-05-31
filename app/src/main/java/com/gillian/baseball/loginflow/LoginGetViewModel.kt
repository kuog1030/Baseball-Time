package com.gillian.baseball.loginflow

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

class LoginGetViewModel(private val repository: BaseballRepository, private val user: User) : ViewModel() {

    // get all unregister player -> select a player -> sign up user -> register selected player -> navigate

    val emptyPlayers = MutableLiveData<List<Player>>()

    val signUpUser = MutableLiveData<User>()

    var team: Team? = null
    var player: Player? = null

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status


    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun getTeamPlayer(teamId: String) {
        viewModelScope.launch {
            val result = repository.getTeamPlayer(teamId)
            when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    getEmptyOnly(result.data)
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    _status.value = LoadStatus.ERROR
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    _status.value = LoadStatus.ERROR
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                }
            }
        }
    }

    fun getEmptyOnly(allPlayer: List<Player>) {
        val resultList: MutableList<Player> = mutableListOf()

        for (player in allPlayer) {
            Log.i("gillian", "player is $player")
            if (player.userId.isNullOrEmpty()) {
                resultList.add(player)
            }
        }
        Log.i("gillian", "empty has ${resultList.size}")
        emptyPlayers.value = resultList
    }


    fun setTeamAndPlayer(myTeam: Team, myPlayer: Player) {
        team = myTeam
        player = myPlayer
    }


    fun signUpUser() {
        if (team == null || player == null) {
            _errorMessage.value =""
        } else {
            _errorMessage.value = null
            viewModelScope.launch {
                user.playerId = player!!.id
                user.teamId = team!!.id
                UserManager.playerId = player!!.id
                UserManager.teamId = team!!.id
                UserManager.team = team

                _status.value = LoadStatus.LOADING
                val result = repository.signUpUser(user)

                signUpUser.value = when (result) {
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
        player?.let {
            viewModelScope.launch {
                val result = repository.registerPlayer(it.id)

                _navigateToTeam.value = when (result) {
                    is Result.Success -> {
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
    }

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

}