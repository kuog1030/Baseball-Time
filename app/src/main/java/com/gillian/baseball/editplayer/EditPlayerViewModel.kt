package com.gillian.baseball.editplayer

import android.net.Uri
import android.util.Log
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
import kotlinx.coroutines.launch


class EditPlayerViewModel(val repository: BaseballRepository) : ViewModel() {

    lateinit var oldPlayer: Player

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    val photoUrl = MutableLiveData<String>()

    val proceedToSave = MutableLiveData<Boolean>(false)

    val readyToSentPhoto = MutableLiveData<Uri>()

    val errorMessage = MutableLiveData<Int>()

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam : LiveData<Boolean>
        get() = _navigateToTeam

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog : LiveData<Boolean>
        get() = _dismissDialog

    fun initOriginInfo(player: Player) {
        oldPlayer = player
        name.value = player.name
        number.value = player.number.toString()
        nickname.value = player.nickname
        photoUrl.value = player.image
    }


    fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            val result = repository.uploadImage(uri)

            photoUrl.value = when (result) {
                is Result.Success -> {
                    proceedToSave.value = true
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

    // with photo -> upload Photo -> proceed to Save -> updatePlayer
    // no photo -> updatePlayer
    fun checkIfInfoFilled() {
        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
            errorMessage.value = R.string.create_new_player_error
        } else {
            errorMessage.value = null
            _status.value = LoadStatus.LOADING
            if (readyToSentPhoto.value != null) {
                Log.i("gillian", "waiting for photo to be uploaded")
                uploadPhoto(readyToSentPhoto.value!!)  // 有照片的話等照片上傳
            } else {
                updatePlayer()
            }
        }
    }


    fun updatePlayer() {
        val numberInt = number.value!!.toInt()

        val newPlayer = Player(
            id = oldPlayer.id,
            name = name.value!!,
            number = numberInt,
            nickname = nickname.value,
            image = photoUrl.value
        )

        Log.i("gillian", "newplayer $newPlayer")

        viewModelScope.launch {
            val uploadResult = repository.updatePlayerInfo(newPlayer)

            _dismissDialog.value = when (uploadResult) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
                    proceedToSave.value = null
                    uploadResult.data
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
        viewModelScope.launch {
            _navigateToTeam.value = when (repository.deletePlayer(oldPlayer.id)) {
                is Result.Success -> true
                else -> null
            }
        }
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


    override fun onCleared() {
        super.onCleared()
        Log.i("gillian", "edit player view model on clear")
    }
}