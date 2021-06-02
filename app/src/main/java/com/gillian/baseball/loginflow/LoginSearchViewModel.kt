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


class LoginSearchViewModel(private val repository: BaseballRepository, private val user: User) : ViewModel() {

    val searchPlayerId = MutableLiveData<String>()

    val playerList = MutableLiveData<List<Player>>()

    var player = MutableLiveData<Player>()

    private val _signUpUser = MutableLiveData<User>()

    val signUpUser: LiveData<User>
        get() = _signUpUser

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToCreate = MutableLiveData<Boolean>()

    val navigateToCreate: LiveData<Boolean>
        get() = _navigateToCreate

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam


    private val _teamCardExpand = MutableLiveData<Boolean>(false)

    val teamCardExpand: LiveData<Boolean>
        get() = _teamCardExpand

    private val _playerCardExpand = MutableLiveData<Boolean>(false)

    val playerCardExpand: LiveData<Boolean>
        get() = _playerCardExpand


    private val _expandNumber = MutableLiveData<Int>(2)

    val expandNumber: LiveData<Int>
        get() = _expandNumber


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

    fun signUpUser() {
        player.value?.let{
            user.playerId = it.id
            user.teamId = it.teamId!!  // 他不會是錯的，因為錯的話隊友那邊根本看球員頁看不到這個人
            UserManager.playerId = it.id
            UserManager.teamId = it.teamId ?: ""
            // 唯一沒拿到的就是完整的team資訊

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
            // view model scope end
        }
    }

    fun registerPlayer() {
        player.value?.let {
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

    fun setExpand(type: Int) {
        _expandNumber.value = type
    }


    fun navigateToCreate() {
        _navigateToCreate.value = true
    }

    fun onCreateNavigated() {
        _navigateToCreate.value = null
    }

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

}