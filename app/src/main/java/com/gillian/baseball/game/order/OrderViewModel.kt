package com.gillian.baseball.game.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch
import java.util.*


class OrderViewModel(private val repository: BaseballRepository) : ViewModel() {

    val selectedSideRadio = MutableLiveData<Int>()
    val gameTitle = MutableLiveData<String>()
    val awayTeamName = MutableLiveData<String>()
    val pitcher = MutableLiveData<String>()

    var lineUp = mutableListOf<EventPlayer>()

    val awayLineUp = mutableListOf<EventPlayer>()

    // TODO() 到時候要改成livedata
    val submitList = MutableLiveData<Boolean>()



    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_order_home -> true
            else -> false
        }

    private val _navigateToGame = MutableLiveData<MyGame>()
    val navigateToGame : LiveData<MyGame>
        get() = _navigateToGame


    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog : LiveData<Boolean>
        get() = _showNewPlayerDialog


    init {
        getTeamPlayer()
    }

    fun getTeamPlayer() {
        viewModelScope.launch {

            val result = repository.getTeamEventPlayer(UserManager.teamId)
            lineUp = when (result) {
                is Result.Success -> {
                    result.data
                }
                is Result.Fail -> {
                    mutableListOf()
                }
                is Result.Error -> {
                    mutableListOf()
                }
                else -> {
                    mutableListOf()
                }
            }
            submitList.value = true
        }
    }

    fun navigateToGame() {
        val game = Game(
                title = gameTitle.value ?: "世界第一武道大會",
                date = Calendar.getInstance().timeInMillis,
                place = "")

        for (index in 1..9) {
            awayLineUp.add(EventPlayer(name = "第${index}棒"))
        }

        val myTeam = GameTeam(
                name = "Android",
                teamId = "999",
                pitcher = EventPlayer(name = pitcher.value ?: "古林睿揚", number = "19", userId = "0019"),
                lineUp = lineUp
        )

        val awayTeam = GameTeam(
                name = awayTeamName.value ?: "iOS",
                pitcher = EventPlayer(name = "對方投手"),
                lineUp = awayLineUp
        )

        game.home = if (isHome) myTeam else awayTeam
        game.guest = if (isHome) awayTeam else myTeam

        viewModelScope.launch {
            repository.createGame(game)
        }


        _navigateToGame.value = MyGame(isHome = isHome, game = game)
    }


    fun showNewPlayerDialog() {
        _showNewPlayerDialog.value = true
    }

    fun onGameNavigated() {
        _navigateToGame.value = null
    }

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

}