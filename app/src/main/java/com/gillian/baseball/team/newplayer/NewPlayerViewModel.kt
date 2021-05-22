package com.gillian.baseball.team.newplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class NewPlayerViewModel(val repository: BaseballRepository) : ViewModel() {

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    val errorMessage = MutableLiveData<Int>()

    var needRefresh = false

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog : LiveData<Boolean>
        get() = _dismissDialog

    fun createPlayer() {
        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
            errorMessage.value = R.string.create_new_player_error
        } else {
            errorMessage.value = null

            val numberInt = number.value!!.toInt()

            val player = Player(
                    teamId = UserManager.teamId,
                    name = name.value!!,
                    number = numberInt,
                    nickname = nickname.value
            )

            viewModelScope.launch {
                repository.createPlayer(player)
            }
            needRefresh = true
            _dismissDialog.value = true
        }
    }

    fun dismissDialog() {
        _dismissDialog.value = true
    }

    fun onDialogDismiss() {
        _dismissDialog.value = null
    }

}