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

    val myStat = MutableLiveData<MyStatistic>()

    val gameBox = MutableLiveData<List<BoxView>>()
    val game = MutableLiveData<Game>()

    var gameId = MutableLiveData<String>()
    var isHome = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        _status.value = LoadStatus.LOADING
    }


    fun fetchGame() {
        viewModelScope.launch {
            val gameResult = repository.getGame(gameId.value!!)
            game.value = when (gameResult) {
                is Result.Success -> {
                    _errorMessage.value = null
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

    fun createBoxView() {
        game.value?.let{
            gameBox.value = it.box.toBoxViewList()
            if (myStat.value != null) _status.value = LoadStatus.DONE
        }
    }


    fun fetchMyTeamStat() {
        viewModelScope.launch {
            val result = repository.getMyGameStat(gameId.value!!, isHome.value ?: false)
            myStat.value = when (result) {
                is Result.Success -> {
                    if (gameBox.value != null) _status.value = LoadStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }
}

/*
    val allStat = MutableLiveData<Statistic>()

    fun getAllStat() {
        viewModelScope.launch {
            val result = repository.getGameStat(gameId.value!!, UserManager.teamId)
            allStat.value = when (result) {
                is Result.Success -> {
                    Log.i("gillian", "all stat")
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

 */