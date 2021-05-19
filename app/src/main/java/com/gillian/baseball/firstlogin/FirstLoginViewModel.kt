package com.gillian.baseball.firstlogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class FirstLoginViewModel(private val repository: BaseballRepository) : ViewModel () {

    val teamName = MutableLiveData<String>()
    val playerName = MutableLiveData<String>()
    val playerNumber = MutableLiveData<String>()

    private val _navigateToTeam = MutableLiveData<Boolean>()
    val navigateToTeam : LiveData<Boolean>
        get() = _navigateToTeam

    var errorMessage = MutableLiveData<String>()



    fun navigateToTeam() {
        if (teamName.value.isNullOrEmpty() || playerName.value.isNullOrEmpty() || playerNumber.value.isNullOrEmpty()) {
            errorMessage.value = "記得3個欄位都要填寫喔"
        } else {
            errorMessage.value = null

            val team = Team(name = teamName.value ?: "")
            val player = Player(name = playerName.value ?: "", number = playerNumber.value ?: "")

            viewModelScope.launch {
                repository.initTeamAndPlayer(team, player)
            }
            _navigateToTeam.value = true
        }
    }

    // 是不是可以不用做on team navigated因為永遠不會再回來這一頁??


}