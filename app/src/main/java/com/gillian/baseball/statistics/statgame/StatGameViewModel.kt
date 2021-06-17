package com.gillian.baseball.statistics.statgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class StatGameViewModel(private val repository: BaseballRepository) : ViewModel() {

    val game = MutableLiveData<Game>()

    val myStat = MutableLiveData<MyStatistic>()

    val gameBox = MutableLiveData<List<BoxView>>()

    private val _status = MutableLiveData<LoadStatus>(LoadStatus.LOADING)

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage


    // fetch Game data -> Create Game Box View
    fun fetchGame(gameId: String) {
        viewModelScope.launch {
            val gameResult = repository.getGame(gameId)
            game.value = when (gameResult) {
                is Result.Success -> {
                    _errorMessage.value = null
                    createBoxView(gameResult.data)
                    gameResult.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = gameResult.error
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = gameResult.exception.toString()
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }


    fun fetchMyTeamStat(gameId: String, isHome: Boolean) {
        viewModelScope.launch {
            val result = repository.getMyGameStat(gameId, isHome)
            myStat.value = when (result) {
                is Result.Success -> {
                    // status will turn DONE only when both stat table and box table are done
                    if (gameBox.value != null) _status.value = LoadStatus.DONE
                    _errorMessage.value = null
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = result.error
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = result.exception.toString()
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    null
                }
            }
        }
    }


    private fun createBoxView(game: Game) {
        gameBox.value = game.box.toBoxViewList()
        // status will turn DONE only when both stat table and box table are done
        if (myStat.value != null) _status.value = LoadStatus.DONE
    }
}