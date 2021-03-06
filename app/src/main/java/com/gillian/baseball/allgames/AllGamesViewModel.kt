package com.gillian.baseball.allgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch

class AllGamesViewModel(private val repository: BaseballRepository) : ViewModel() {

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

    private val _scoresGames = MutableLiveData<List<GameCard>>()

    val scoresGames : LiveData<List<GameCard>>
        get() = _scoresGames

    private val _scheduleGames = MutableLiveData<List<GameCard>>()

    val scheduleGames : LiveData<List<GameCard>>
        get() = _scheduleGames

    // record the status of floating action button
    val clicked = MutableLiveData<Boolean>(false)

    val totalWins = MutableLiveData<Int>(0)
    val totalLose = MutableLiveData<Int>(0)
    val teamName = MutableLiveData<String>(UserManager.team?.name)


    init {
        getAllGamesCard()
    }


    fun expandAndClose() {
        clicked.value = (!clicked.value!!)
    }


    private fun getAllGamesCard() {
        viewModelScope.launch {
            _status.value = LoadStatus.LOADING
            when (val result = repository.getAllGamesCard(UserManager.teamId)) {
                is Result.Success -> {
                    _errorMessage.value = null
                    _status.value = LoadStatus.DONE
                    separateFinishedGame(result.data)
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    _status.value = LoadStatus.ERROR
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    _status.value = LoadStatus.ERROR
                }
                else -> {
                    _errorMessage.value = getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                }
            }
        }
    }

    private fun separateFinishedGame(gameCards: List<GameCard>) {
        val endGames = mutableListOf<GameCard>()
        val yetGames = mutableListOf<GameCard>()

        for (game in gameCards) {
            if (game.status >= GameStatus.FINAL.number) {
                // including final and final with stat
                endGames.add(game)
            } else if (game.status == GameStatus.SCHEDULED.number) {
                yetGames.add(game)
            }
        }

        endGames.sortByDescending { it.date }
        yetGames.sortBy { it.date }
        totalWins.value = endGames.count { it.gameResult == GameResult.WIN }
        totalLose.value = endGames.count {it.gameResult == GameResult.LOSE}

        _scoresGames.value = endGames
        _scheduleGames.value = yetGames
        _refreshStatus.value = false

    }

    fun refresh() {
        if (_status.value != LoadStatus.LOADING) {
            getAllGamesCard()
        }
    }

    fun deleteGame(gameId: String) {
        viewModelScope.launch {
            val result = repository.deleteGame(gameId)
            refresh()
        }
    }


    fun startANewGame() {
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
