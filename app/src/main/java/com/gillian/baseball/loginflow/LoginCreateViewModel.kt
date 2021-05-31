package com.gillian.baseball.loginflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class LoginCreateViewModel(private val repository: BaseballRepository) : ViewModel() {

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
            errorMessage.value = Util.getString(R.string.error_init_team_player)
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