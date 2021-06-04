package com.gillian.baseball.game.pinch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.R
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util.getString


class PinchViewModel(private val repository: BaseballRepository) : ViewModel() {

    val expandBlock = MutableLiveData<Int>(0)

    val playerToggleText = MutableLiveData<String>("")

    private val _dismissDialog = MutableLiveData<Boolean>()

    val dismissDialog : LiveData<Boolean>
        get() = _dismissDialog

    fun expandPlayer() {
        expandBlock.value = when (expandBlock.value) {
            1 -> 0
            else -> 1
        }
    }

    fun expandError() {
        expandBlock.value = when (expandBlock.value) {
            2 -> 0
            else -> 2
        }
    }

    fun setToggleText(isDefence : Boolean) {
        playerToggleText.value = if(isDefence) getString(R.string.pinch_pitcher) else getString(R.string.pinch_hitter)
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
