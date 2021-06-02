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


class LoginFirstViewModel(private val repository: BaseballRepository, private val user: User) : ViewModel() {

    val searchPlayerId = MutableLiveData<String>()

    val playerList = MutableLiveData<List<Player>>()

    var player = MutableLiveData<Player>()



    val newTeamName = MutableLiveData<String>()

    val newPlayerName = MutableLiveData<String>()

    val newPlayerNumber = MutableLiveData<String>()




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

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam

    private val _navigateFromRegister = MutableLiveData<Boolean>()

    val navigateFromRegister: LiveData<Boolean>
        get() = _navigateFromRegister




    fun searchPlayer() {
        Log.i("gillian", "search player ${searchPlayerId.value}")
        if (searchPlayerId.value.isNullOrEmpty()) {
            playerList.value = emptyList()

        } else {
            viewModelScope.launch {
                val result = repository.searchPlayer(searchPlayerId.value!!)
                playerList.value = when(result) {
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
    }

    fun signUpUserFromRegister() {
        player.value?.let{
            user.playerId = it.id
            user.teamId = it.teamId!!  // 他不會是錯的，因為錯的話隊友那邊根本看球員頁看不到這個人
            UserManager.playerId = it.id
            UserManager.teamId = it.teamId ?: ""
            // 唯一沒拿到的就是完整的team資訊

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
            // view model scope end
        }
    }

    fun registerPlayer() {
        player.value?.let {
            viewModelScope.launch {
                val result = repository.registerPlayer(it.id)

                _navigateFromRegister.value = when (result) {
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

    fun signUpUser() {
        if (newTeamName.value.isNullOrEmpty() || newPlayerNumber.value.isNullOrEmpty() || newPlayerName.value == null) {
            _errorMessage.value = Util.getString(R.string.error_init_team_player)
        } else {
            _errorMessage.value = null

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
                        _errorMessage.value = Util.getString(R.string.return_nothing)
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
            val result = repository.initTeamAndPlayer(team, player)
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

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

    fun fromRegisterNavigated() {
        _navigateFromRegister.value = null
    }

}