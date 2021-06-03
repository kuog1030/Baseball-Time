package com.gillian.baseball.broadcast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class BroadcastViewModel(private val repository: BaseballRepository, val game: GameCard) : ViewModel() {


    var liveEvents = MutableLiveData<List<Event>>()

    val homeScore = MutableLiveData<Int>()

    val guestScore = MutableLiveData<Int>()


    init {
        getLiveEvent()
        if (game.place.isEmpty()) {
            game.place = getString(R.string.no_game_place)
        }
    }

    fun getLiveEvent() {
        viewModelScope.launch {
            liveEvents = repository.getLiveEvents(game.id)
        }
    }


}