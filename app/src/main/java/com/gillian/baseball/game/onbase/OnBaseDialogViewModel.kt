package com.gillian.baseball.game.onbase

import android.util.Log
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

    val errorLayout = MutableLiveData<Boolean>(false)

    private var _proceedWithError = MutableLiveData<Boolean>()
    val proceedWithError: LiveData<Boolean>
        get() = _proceedWithError

    private var _onBaseOut = MutableLiveData<EventType>()

    val onBaseOut: LiveData<EventType>
        get() = _onBaseOut

    fun pickOff() {
        _onBaseOut.value = EventType.PICKOFF
    }

    fun stealBaseSuccess() {
        _proceedWithError.value = false
        viewModelScope.launch {
            repository.sendEvent(onBaseInfo.gameId, Event(player = player!!, pitcher = pitcher!!, result = EventType.STEALBASE.number, inning = onBaseInfo.inning))
        }
    }

    fun stealBaseFail() {
        _onBaseOut.value = EventType.STEALBASEFAIL
    }

    fun advanceByError(expand: Boolean) {
        if (onBaseInfo.isDefence) {
            errorLayout.value = expand
        } else {
            _proceedWithError.value = true
        }
    }

    fun recordError(onClickPlayer: EventPlayer) {
        viewModelScope.launch {
            repository.sendEvent(onBaseInfo.gameId, Event(player = onClickPlayer, result = EventType.ERROR.number, inning = onBaseInfo.inning))
        }
        _proceedWithError.value = true
    }

    fun advance() {
        _proceedWithError.value = false
    }

    fun onProceedDone() {
        _proceedWithError.value = null
    }

    fun onOutDone() {
        _onBaseOut.value = null
    }
}