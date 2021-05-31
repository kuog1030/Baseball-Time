package com.gillian.baseball.loginflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class LoginCreateViewModel(private val repository: BaseballRepository, private val user: User) : ViewModel() {

    val teamName = MutableLiveData<String>()
    val playerName = MutableLiveData<String>()
    val playerNumber = MutableLiveData<String>()

    var goToTeam = false

    val signUpUser = MutableLiveData<User>()

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToTeamOrOrder = MutableLiveData<Boolean>()

    val navigateToTeamOrOrder: LiveData<Boolean>
        get() = _navigateToTeamOrOrder


    // sign up user -> init team and player

    fun signUpUser(nextToTeam: Boolean) {
        if (teamName.value.isNullOrEmpty() || playerName.value.isNullOrEmpty() || playerNumber.value == null) {
            _errorMessage.value = Util.getString(R.string.error_init_team_player)
        } else {
            goToTeam = nextToTeam
            _errorMessage.value = null

            viewModelScope.launch {
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


    fun navigateToTeam() {
        val numberInt = playerNumber.value!!.toInt()

        val team = Team(name = teamName.value ?: "")
        val player = Player(name = playerName.value ?: "", number = numberInt, image = user.image)

        viewModelScope.launch {
            val result = repository.initTeamAndPlayer(team, player)
            _navigateToTeamOrOrder.value = when (result) {
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

    fun onNavigatedDone() {
        _navigateToTeamOrOrder.value = null
    }


}