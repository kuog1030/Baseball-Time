package com.gillian.baseball.newplayer

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
import kotlinx.coroutines.launch

class NewPlayerViewModel(val repository: BaseballRepository) : ViewModel() {

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    val photoUrl = MutableLiveData<String>()

    val errorMessage = MutableLiveData<Int>()

    val readyToSentPhoto = MutableLiveData<Uri>()

    val proceedToSave = MutableLiveData<Boolean>(false)

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    var needRefresh = false

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog : LiveData<Boolean>
        get() = _dismissDialog

    fun createPlayer() {
        errorMessage.value = null

        val numberInt = number.value!!.toInt()

        val player = Player(
                teamId = UserManager.teamId,
                name = name.value!!,
                number = numberInt,
                nickname = nickname.value ?: name.value!!,
                image = photoUrl.value
        )

        viewModelScope.launch {
            val result = repository.createPlayer(player)
            _dismissDialog.value = when (result) {
                is Result.Success -> {
                    Log.i("gillian", "in coroutine create success")
                    _status.value = LoadStatus.DONE
                    needRefresh = true
                    proceedToSave.value = null
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

    // with photo -> upload Photo -> proceed to Save -> createPlayer
    // no photo -> createPlayer
    fun checkIfInfoFilled() {
        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
            errorMessage.value = R.string.create_new_player_error
        } else {
            errorMessage.value = null
            _status.value = LoadStatus.LOADING
            if (readyToSentPhoto.value != null) {
                uploadPhoto(readyToSentPhoto.value!!)  // 有照片的話等照片上傳
            } else {
                createPlayer()
            }
        }
    }



    fun uploadPhoto(uri: Uri) {
        Log.i("gillian", "upload photo")
        viewModelScope.launch {
            val result = repository.uploadImage(uri)
            photoUrl.value = when (result) {
                is Result.Success -> {
                    proceedToSave.value = true
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    Log.i("gillian", "fail ${result.error}")
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    Log.i("gillian", "error ${result.exception}")
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun dismissDialog() {
        _dismissDialog.value = true
    }

    fun onDialogDismiss() {
        _dismissDialog.value = null
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("gillian", "new player view model on clear")
    }
}