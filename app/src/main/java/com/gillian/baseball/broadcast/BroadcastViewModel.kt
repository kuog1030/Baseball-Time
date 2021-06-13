package com.gillian.baseball.broadcast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class BroadcastViewModel(private val repository: BaseballRepository, val game: GameCard) : ViewModel() {


    var liveEvents = MutableLiveData<List<Event>>()

    var liveGame = MutableLiveData<Game>()

    val editable = MutableLiveData<Boolean>(false)

    private val _turnOffBroadcast = MutableLiveData<Boolean>()

    val turnOffBroadcast : LiveData<Boolean>
        get() = _turnOffBroadcast

    init {
        fetchLiveEvent()
        fetchLiveGame()
        if (game.place.isEmpty()) {
            game.place = getString(R.string.no_game_place)
        }
        if (game.recordedTeamId == UserManager.teamId) {
            editable.value = true
        }
    }

    fun enableBroadcast() {
        viewModelScope.launch {
            val result = repository.updateGameStatus(game.id, GameStatus.PLAYINGPRIVATE)
            _turnOffBroadcast.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                is Result.Fail -> {
                    null
                }
                is Result.Error -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
    }


    fun onBroadcastOff() {
        _turnOffBroadcast.value = null
    }

    private fun fetchLiveEvent() {
        viewModelScope.launch {
            liveEvents = repository.getLiveEvents(game.id)
        }
    }

    private fun fetchLiveGame() {
        viewModelScope.launch {
            liveGame = repository.getLiveGame(game.id)
        }
    }


}