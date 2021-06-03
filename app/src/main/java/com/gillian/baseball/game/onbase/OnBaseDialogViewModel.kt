package com.gillian.baseball.game.onbase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.OnBaseInfo
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.EventType
import kotlinx.coroutines.launch

class OnBaseDialogViewModel(private val repository: BaseballRepository, private val onBaseInfo : OnBaseInfo) : ViewModel() {

    var player = onBaseInfo.baseList[onBaseInfo.onClickPlayer]
    var pitcher = onBaseInfo.pitcher
    var atBase = AtBase(base = onBaseInfo.onClickPlayer, player = player!!)

    private var _proceed = MutableLiveData<Boolean>()
    val proceed: LiveData<Boolean>
        get() = _proceed

    private var _onBaseOut = MutableLiveData<EventType>()

    val onBaseOut: LiveData<EventType>
        get() = _onBaseOut

    fun pickOff() {
        _onBaseOut.value = EventType.PICKOFF
    }

    fun stealBaseSuccess() {
        _proceed.value = true
        viewModelScope.launch {
            repository.sendEvent(onBaseInfo.gameId, Event(player = player!!, pitcher = pitcher!!, result = EventType.STEALBASE.number))
        }
    }

    fun stealBaseFail() {
        _onBaseOut.value = EventType.STEALBASEFAIL
    }

    fun advanceByError() {
        _proceed.value = true
    }

    fun advance() {
        _proceed.value = true
    }

    fun onProceedDone() {
        _proceed.value = null
    }

    fun onOutDone() {
        _onBaseOut.value = null
    }
}