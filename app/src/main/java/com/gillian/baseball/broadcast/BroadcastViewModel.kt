package com.gillian.baseball.broadcast

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

    // register snapshot listener for getting live events
    var liveEvents = MutableLiveData<List<Event>>()

    // register snapshot listener for
    var liveGame = MutableLiveData<Game>()

    // editable only when the game is recorded by user
    val editable = MutableLiveData<Boolean>(false)

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _stopBroadcast = MutableLiveData<Boolean>()

    val stopBroadcast: LiveData<Boolean>
        get() = _stopBroadcast

    init {
        fetchLiveEvent()
        fetchLiveGame()
        if (game.place.isEmpty()) {
            game.place = getString(R.string.no_game_place)
        }
        editable.value = (game.recordedTeamId == UserManager.teamId)
    }

    fun stopBroadcast() {
        viewModelScope.launch {
            val result = repository.updateGameStatus(game.id, GameStatus.PLAYINGPRIVATE)
            _stopBroadcast.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    result.data
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


    fun onBroadcastOff() {
        _stopBroadcast.value = null
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