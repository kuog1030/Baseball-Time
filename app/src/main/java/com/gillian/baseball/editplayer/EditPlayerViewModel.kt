package com.gillian.baseball.editplayer

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
import com.gillian.baseball.util.Util
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch


class EditPlayerViewModel(val repository: BaseballRepository) : ViewModel() {

    lateinit var oldPlayer: Player

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    val photoUrl = MutableLiveData<String>()

    val photoToBeSent = MutableLiveData<Uri>()

    val confirmDelete = MutableLiveData<Boolean>(false)

    // only when this player is user itself can wipe player's accumulated data
    val isMe = MutableLiveData<Boolean>(false)

    private val _newImage = MutableLiveData<String>()

    val newImage: LiveData<String>
        get() = _newImage

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog: LiveData<Boolean>
        get() = _dismissDialog


    fun initOriginInfo(player: Player) {
        player.let {
            oldPlayer = it
            name.value = it.name
            number.value = it.number.toString()
            nickname.value = it.nickname
            photoUrl.value = it.image

            isMe.value = (it.id == UserManager.playerId)
        }
    }


    // check if info filled -> (upload photo) -> updatePlayer
    fun checkIfInfoFilled() {

        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
            _errorMessage.value = getString(R.string.create_new_player_error)
        } else {
            _errorMessage.value = null
            _status.value = LoadStatus.LOADING

            if (photoToBeSent.value != null) {
                uploadPhoto(photoToBeSent.value!!)
            } else {
                _newImage.value = photoUrl.value ?: ""
            }
        }
    }


    private fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            val result = repository.uploadImage(uri)

            _newImage.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }


    fun updatePlayer(imageUrl: String) {
        val numberInt = number.value!!.toInt()

        val newPlayer = Player(
                id = oldPlayer.id,
                name = name.value!!,
                number = numberInt,
                nickname = nickname.value ?: name.value!!,
                image = imageUrl
        )

        viewModelScope.launch {
            val result = repository.updatePlayerInfo(newPlayer)

            _dismissDialog.value = when (result) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun confirmDelete() {
        viewModelScope.launch {
            _navigateToTeam.value = when (val result = repository.deletePlayer(oldPlayer.id)) {
                is Result.Success -> {
                    _errorMessage.value = null
                    true
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
                    _errorMessage.value = getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    fun clearMyData() {
        _status.value = LoadStatus.LOADING
        viewModelScope.launch {
            val result = repository.clearMyStat(UserManager.playerId, name.value ?: "")
            _dismissDialog.value = when (result) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }


    fun deletePlayer() {
        confirmDelete.value = true
    }

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

    fun dismissAndDontSave() {
        _dismissDialog.value = false
    }

    fun onDialogDismiss() {
        _dismissDialog.value = null
    }

}