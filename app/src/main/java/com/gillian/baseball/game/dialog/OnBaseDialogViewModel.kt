package com.gillian.baseball.game.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.EventType
import kotlinx.coroutines.launch

class OnBaseDialogViewModel(private val repository: BaseballRepository) : ViewModel() {

    var player : EventPlayer? = null

    private var _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean>
        get() = _dismiss

    private var _proceed = MutableLiveData<Boolean>()
    val proceed: LiveData<Boolean>
        get() = _proceed

    private var _onBaseOut = MutableLiveData<Boolean>()
    val onBaseOut: LiveData<Boolean>
        get() = _onBaseOut

    fun pickOff() {
        _onBaseOut.value = true
        dismissDialog()
    }

    fun stealBaseSuccess() {
        _proceed.value = true
        viewModelScope.launch {
            repository.sendEvent(Event(player = player!!, result = EventType.STEALBASE.number))
        }
        dismissDialog()
    }

    fun stealBaseFail() {
        _onBaseOut.value = true
        dismissDialog()
    }

    fun advanceByError() {
        _proceed.value = true
    }

    fun advance() {
        _proceed.value = true
        dismissDialog()
    }

    fun dismissDialog() {
        _dismiss.value = true
    }

    fun onDialogDismiss() {
        _dismiss.value = null
    }

    fun onProceedDone() {
        _proceed.value = null
    }

    fun onOutDone() {
        _onBaseOut.value = null
    }
}