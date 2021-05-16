package com.gillian.baseball.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.R
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.GameTeam
import com.gillian.baseball.data.source.BaseballRepository
import java.util.*


class OrderViewModel(private val repository: BaseballRepository) : ViewModel() {

    val selectedSideRadio = MutableLiveData<Int>()
    val gameTitle = MutableLiveData<String>()
    val awayTeam = MutableLiveData<String>()

    // line up player
    val first = MutableLiveData<String>()
    val second = MutableLiveData<String>()
    val third = MutableLiveData<String>()
    val forth = MutableLiveData<String>()
    val fifth = MutableLiveData<String>()
    val sixth = MutableLiveData<String>()
    val seventh = MutableLiveData<String>()
    val eighth = MutableLiveData<String>()
    val ninth = MutableLiveData<String>()

    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_order_home -> true
            else -> false
        }

    private val _navigateToGame = MutableLiveData<Game>()
    val navigateToGame : LiveData<Game>
        get() = _navigateToGame



    fun navigateToGame() {
        val game = Game(
                name = gameTitle.value ?: "",
                date = Calendar.getInstance().timeInMillis,
                place = "")
        val nameList = listOf(first.value, second.value, third.value, forth.value, fifth.value, sixth.value, seventh.value, eighth.value, ninth.value)
        val lineUp = mutableListOf<EventPlayer>()

        for (name in nameList) {
            while (name != null) {
                lineUp.add(EventPlayer(name = name))
            }
        }

        val team = GameTeam(
                name = "Android",
                teamId = "999",
                lineUp = lineUp
        )

        if (isHome) {
            game.home = team
        } else {
            game.guest = team
        }

        _navigateToGame.value = game
    }

    fun onGameNavigated() {
        _navigateToGame.value = null
    }

}