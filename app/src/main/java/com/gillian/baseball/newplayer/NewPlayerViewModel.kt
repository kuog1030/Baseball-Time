package com.gillian.baseball.newplayer

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class NewPlayerViewModel(val repository: BaseballRepository) : ViewModel() {

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    private val _photoUrl = MutableLiveData<String>()

    val photoUrl: LiveData<String>
        get() = _photoUrl

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    val photoToBeSent = MutableLiveData<Uri>()

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    var needRefresh = false

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog : LiveData<Boolean>
        get() = _dismissDialog

    // check if info filled -> (upload photo) -> create player
    fun checkIfInfoFilled() {
        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
            _errorMessage.value = getString(R.string.create_new_player_error)
        } else {
            _errorMessage.value = null
            _status.value = LoadStatus.LOADING

            if (photoToBeSent.value != null) {
                uploadPhoto(photoToBeSent.value!!)
            } else {
                _photoUrl.value = ""
            }
        }
    }

    private fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            val result = repository.uploadImage(uri)
            _photoUrl.value = when (result) {
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

    fun createPlayer() {
        val player = initiateNewPlayer()

        viewModelScope.launch {
            val result = repository.createPlayer(player)
            _dismissDialog.value = when (result) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
                    _errorMessage.value = null
                    needRefresh = true
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = result.error
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = result.exception.toString()
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    private fun initiateNewPlayer() : Player {
        val nickname = if (nickname.value.isNullOrEmpty()) name.value!! else nickname.value!!

        return Player(
                teamId = UserManager.teamId,
                name = name.value!!,
                number = number.value!!.toInt(),
                nickname = nickname,
                image = photoUrl.value
        )
    }

    fun dismissDialog() {
        _dismissDialog.value = true
    }

    fun onDialogDismiss() {
        _dismissDialog.value = null
    }

}