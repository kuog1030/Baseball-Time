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
import kotlinx.coroutines.launch


class EditPlayerViewModel(val repository: BaseballRepository) : ViewModel() {
//
//    val name = MutableLiveData<String>()
//
//    val number = MutableLiveData<String>()
//
//    val nickname = MutableLiveData<String>()
//
//    val photoUrl = MutableLiveData<String>()
//
//    val errorMessage = MutableLiveData<Int>()
//
//    private val _status = MutableLiveData<LoadStatus>()
//
//    val status: LiveData<LoadStatus>
//        get() = _status
//
//    private val _dismissDialog = MutableLiveData<Boolean>()
//
//    val dismissDialog : LiveData<Boolean>
//        get() = _dismissDialog
//
//    fun createPlayer() {
//        if (name.value.isNullOrEmpty() || number.value.isNullOrEmpty()) {
//            errorMessage.value = R.string.create_new_player_error
//        } else {
//
//            if (_status.value != LoadStatus.LOADING) {
//                errorMessage.value = null
//
//                val numberInt = number.value!!.toInt()
//
//                val player = Player(
//                    teamId = UserManager.teamId,
//                    name = name.value!!,
//                    number = numberInt,
//                    nickname = nickname.value,
//                    image = photoUrl.value
//                )
//
//                viewModelScope.launch {
//                    repository.createPlayer(player)
//                    Log.i("gillian", "in coroutine create success")
//                }
//                needRefresh = true
//                _dismissDialog.value = true
//            }
//        }
//    }
//
//    fun uploadPhoto(uri: Uri) {
//        Log.i("gillian", "upload photo")
//        viewModelScope.launch {
//
//            _status.value = LoadStatus.LOADING
//
//            val result = repository.uploadImage(uri)
//
//            photoUrl.value = when (result) {
//                is Result.Success -> {
//                    _status.value = LoadStatus.DONE
//                    Log.i("gillian", "success")
//                    result.data
//                }
//                is Result.Fail -> {
//                    _status.value = LoadStatus.ERROR
//                    Log.i("gillian", "fail ${result.error}")
//                    null
//                }
//                is Result.Error -> {
//                    _status.value = LoadStatus.ERROR
//                    Log.i("gillian", "error ${result.exception}")
//                    null
//                }
//                else -> {
//                    _status.value = LoadStatus.ERROR
//                    null
//                }
//            }
//        }
//    }
//
//    fun dismissDialog() {
//        _dismissDialog.value = true
//    }
//
//    fun onDialogDismiss() {
//        _dismissDialog.value = null
//    }
//
//
//    override fun onCleared() {
//        super.onCleared()
//        Log.i("gillian", "new player view model on clear")
//    }
}