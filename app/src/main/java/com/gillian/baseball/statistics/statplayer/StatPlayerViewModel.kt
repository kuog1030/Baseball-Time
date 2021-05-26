package com.gillian.baseball.statistics.statplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.data.PitcherBox
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class StatPlayerViewModel(private val repository: BaseballRepository) : ViewModel() {

    val player = MutableLiveData<Player>()

    val hitStat = MutableLiveData<HitterBox>()

    val pitchStat = MutableLiveData<PitcherBox>()


    fun getPlayerStat(playerId: String) {
        viewModelScope.launch {

        }
    }

}