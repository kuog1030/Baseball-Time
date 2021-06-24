package com.gillian.baseball.game.pinch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.GameStatus
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch


class PinchViewModel(private val repository: BaseballRepository) : ViewModel() {

    val expandBlock = MutableLiveData<Boolean>(false)

    val playerToggleText = MutableLiveData<String>("")

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog: LiveData<Boolean>
        get() = _dismissDialog

    fun expandPlayer() {
        expandBlock.value = !(expandBlock.value!!)
    }

    fun setToggleText(isDefence: Boolean) {
        playerToggleText.value = if (isDefence) getString(R.string.pinch_pitcher) else getString(R.string.pinch_hitter)
    }

    fun startBroadcast(gameId: String) {
        viewModelScope.launch {
            val result = repository.updateGameStatus(gameId, GameStatus.PLAYING)
            when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                }
                else -> {
                    _errorMessage.value = getString(R.string.return_nothing)
                }
            }
        }
    }

    fun dismiss() {
        _dismissDialog.value = true
    }

    fun onDialogDismiss() {
        _dismissDialog.value = null
    }

    fun nothing() {

    }

}
