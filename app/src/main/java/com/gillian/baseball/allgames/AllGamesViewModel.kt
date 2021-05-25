package com.gillian.baseball.allgames

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util.getAnim
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class AllGamesViewModel(private val repository: BaseballRepository) : ViewModel() {

    var currentTime = System.currentTimeMillis()

    private val _startNewGame = MutableLiveData<Boolean>()
    val startNewGame : LiveData<Boolean>
        get() = _startNewGame

    private val _navigateToNewGame = MutableLiveData<Boolean>()
    val navigateToNewGame : LiveData<Boolean>
        get() = _navigateToNewGame

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _allGameCards = MutableLiveData<List<GameCard>>()

    val allGameCards : LiveData<List<GameCard>>
        get() = _allGameCards

    private val _scoresGames = MutableLiveData<List<GameCard>>()

    val scoresGames : LiveData<List<GameCard>>
        get() = _scoresGames

    private val _scheduleGames = MutableLiveData<List<GameCard>>()

    val scheduleGames : LiveData<List<GameCard>>
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

            _status.value = LoadStatus.LOADING

            val result = repository.getAllGamesCard("")

            _allGameCards.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    _status.value = LoadStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _errorMessage.value = getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun seperateFinishedGame() {
        val endGames = mutableListOf<GameCard>()
        val yetGames = mutableListOf<GameCard>()
        for (game in _allGameCards.value!!) {
            if (game.status == GameStatus.FINAL.number) {
                endGames.add(game)
            } else {
                yetGames.add(game)
            }
        }
        endGames.sortByDescending { it.date }
        yetGames.sortBy { it.date }
        _scoresGames.value = endGames
        _scheduleGames.value = yetGames
        _refreshStatus.value = false

    }

    fun refresh() {
        if (_status.value != LoadStatus.LOADING) {
            _refreshStatus.value = true
            getAllGamesCard()
        }
    }


    fun startANewGame() {
        //TODO()這邊應該要傳個東西過去
        _startNewGame.value = true
    }

    fun onNewGameStarted() {
        _startNewGame.value = null
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