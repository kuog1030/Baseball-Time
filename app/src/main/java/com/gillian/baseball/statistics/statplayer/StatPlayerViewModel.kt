package com.gillian.baseball.statistics.statplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class StatPlayerViewModel(private val repository: BaseballRepository) : ViewModel() {

    val player = MutableLiveData<Player>()

    // player will be editable if (1) it is user itself (2) has no user registered (user id is null)
    val editable = MutableLiveData<Boolean>(false)

    var isUser = MutableLiveData<Boolean>(false)

    // determine whether player's user id to be shown
    val noUserRegistered = MutableLiveData<Boolean>(false)

    val infoVisibility = MutableLiveData<Boolean>(false)

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToEdit = MutableLiveData<Player>()

    val navigateToEdit: LiveData<Player>
        get() = _navigateToEdit

    private val _navigateToLogin = MutableLiveData<Boolean>()

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin


    fun fetchPlayerStat(playerId: String) {
        viewModelScope.launch {
            val result = repository.getOnePlayer(playerId)

            player.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    updateEditable(result.data)
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

    private fun updateEditable(player: Player) {
        player.let {
            editable.value = (it.userId.isNullOrEmpty() || it.userId == UserManager.userId)
            noUserRegistered.value = it.userId.isNullOrEmpty()
            isUser.value = (UserManager.userId == it.userId)
        }
    }

    fun showInfo(toShow: Boolean = true) {
        if (toShow) {
            // for toggle button - switch between on or off
            infoVisibility.value = !(infoVisibility.value!!)
        } else {
            infoVisibility.value = false
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            val result = repository.deleteUser(UserManager.userId)
            _navigateToLogin.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
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

    fun refresh() {
        player.value?.let {
            fetchPlayerStat(it.id)
        }
    }

    fun navigateToEdit() {
        _navigateToEdit.value = player.value
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
    }
}