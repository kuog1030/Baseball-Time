package com.gillian.baseball.allgames

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util.getAnim
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class AllGamesViewModel(private val repository: BaseballRepository) : ViewModel() {

    var currentTime = System.currentTimeMillis()

    private val _navigateToOrder = MutableLiveData<Boolean>()
    val navigateToOrder : LiveData<Boolean>
        get() = _navigateToOrder

    private val _navigateToNewGame = MutableLiveData<Boolean>()
    val navigateToNewGame : LiveData<Boolean>
        get() = _navigateToNewGame

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _scoresGames = MutableLiveData<List<GameCard>>()

    val scoresGames : LiveData<List<GameCard>>
        get() = _scoresGames

    private val _scheduleGames = MutableLiveData<List<Game>>()

    val scheduleGames : LiveData<List<Game>>
        get() = _scheduleGames


    val clicked = MutableLiveData<Boolean>(false)




    init {
        getAllGamesCard()
    }


    fun expandAndClose() {
        clicked.value = (!clicked.value!!)
    }


    fun getAllGamesCard() {
        viewModelScope.launch {
            val result = repository.getAllGamesCard("")

            _scoresGames.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    Log.i("gillian", "wow ${result.data}")
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



    fun startANewGame() {
        //TODO()這邊應該要傳個東西過去
        _navigateToOrder.value = true
    }

    fun onNewGameStarted() {
        _navigateToOrder.value = null
    }

    fun createNewGame() {
        _navigateToNewGame.value = true
    }

    fun onNewGameCreated() {
        _navigateToNewGame.value = null
    }


}

/*
    fun getMyTeam() {
        viewModelScope.launch {
            val result = repository.getTeam2(UserManager.teamId)

            _myTeam.value = when(result) {
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
 */