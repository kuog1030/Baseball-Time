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
import kotlinx.coroutines.launch

class FirstLoginViewModel(private val repository: BaseballRepository) : ViewModel () {

    val teamName = MutableLiveData<String>()
    val playerName = MutableLiveData<String>()
    val playerNumber = MutableLiveData<String>()

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam : LiveData<Boolean>
        get() = _navigateToTeam

    private val _navigateToOrder = MutableLiveData<Boolean>()

    val navigateToOrder : LiveData<Boolean>
        get() = _navigateToOrder

    var errorMessage = MutableLiveData<String>()



    fun navigateToTeam(toTeam: Boolean) {
        if (teamName.value.isNullOrEmpty() || playerName.value.isNullOrEmpty() || playerNumber.value == null) {
            errorMessage.value = "記得3個欄位都要填寫喔"
        } else {
            errorMessage.value = null


            val numberInt = playerNumber.value!!.toInt()

            val team = Team(name = teamName.value ?: "")
            val player = Player(name = playerName.value ?: "", number = numberInt)

            viewModelScope.launch {
                val result = repository.initTeamAndPlayer(team, player)
                Log.i("gillian", "view model scope called")
                if (toTeam) {
                    _navigateToTeam.value = when (result) {
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
    }

    fun onNavigatedDone() {
        //_navigateToOrder.value = null
        _navigateToTeam.value = null
    }

}