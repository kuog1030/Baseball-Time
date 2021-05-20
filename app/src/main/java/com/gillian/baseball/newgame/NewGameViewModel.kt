package com.gillian.baseball.newgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class NewGameViewModel(val repository: BaseballRepository) : ViewModel() {

    val homeName = MutableLiveData<String>()
    val guestName = MutableLiveData<String>()

    val awayName = MutableLiveData<String>()
    val gameTitle = MutableLiveData<String>()
    val gamePlace = MutableLiveData<String>()

    val selectedSideRadio = MutableLiveData<Int>()

    private val _teamPlayers = MutableLiveData<MutableList<Player>>()
    val teamPlayers: LiveData<MutableList<Player>>
        get() = _teamPlayers

    // TODO()這邊不要hard coded!
    val gameDate = MutableLiveData<String>("選個日期吧")

    fun changeSide() {
        when (selectedSideRadio.value) {
            R.id.radio_new_game_home -> {
                homeName.value = UserManager.teamName
                guestName.value = gameTitle.value
            }
            else -> {
                homeName.value = gameTitle.value
                guestName.value = UserManager.teamName
            }

        }
    }

    fun formatDate(year : Int, month : Int, dayOfMonth : Int) {
        gameDate.value = "${year}-${month+1}-${dayOfMonth}"
    }

    fun getTeamPlayer() {

        viewModelScope.launch {

            val result = repository.getTeamPlayer(UserManager.teamId)

            _teamPlayers.value = when (result) {
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



//    private val isHome: Boolean
//        get() = when (selectedSideRadio.value) {
//            R.id.radio_order_home -> true
//            else -> false
//        }

}