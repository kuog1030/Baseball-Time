package com.gillian.baseball.firstlogin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class FirstLoginViewModel(private val repository: BaseballRepository) : ViewModel() {

    val teamName = MutableLiveData<String>()
    val playerName = MutableLiveData<String>()
    val playerNumber = MutableLiveData<String>()

    var goToTeam = false

    private val _navigateToTeamOrOrder = MutableLiveData<Boolean>()

    val navigateToTeamOrOrder: LiveData<Boolean>
        get() = _navigateToTeamOrOrder

    var errorMessage = MutableLiveData<String>()


    fun navigateToTeam(toTeam: Boolean) {
        if (teamName.value.isNullOrEmpty() || playerName.value.isNullOrEmpty() || playerNumber.value == null) {
            errorMessage.value = getString(R.string.error_init_team_player)
        } else {
            errorMessage.value = null

            goToTeam = toTeam
            val numberInt = playerNumber.value!!.toInt()

            val team = Team(name = teamName.value ?: "")
            val player = Player(name = playerName.value ?: "", number = numberInt)

            viewModelScope.launch {
                val result = repository.initTeamAndPlayer(team, player)
                _navigateToTeamOrOrder.value = when (result) {
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
    }

    fun onNavigatedDone() {
        _navigateToTeamOrOrder.value = null
    }

}