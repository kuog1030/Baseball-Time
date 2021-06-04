package com.gillian.baseball.statistics.statgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class StatGameViewModel(private val repository: BaseballRepository) : ViewModel() {

    val myStat = MutableLiveData<MyStatistic>()

    val gameBox = MutableLiveData<List<BoxView>>()

    var gameId = MutableLiveData<String>()
    var isHome = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    init {
        _status.value = LoadStatus.LOADING
    }


    fun fetchGameBox() {
        viewModelScope.launch {
            val boxResult = repository.getGameBox(gameId.value!!)
            gameBox.value = when (boxResult) {
                is Result.Success -> {
                    boxResult.data.toBoxViewList()
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


    fun fetchMyTeamStat() {
        viewModelScope.launch {
            val result = repository.getMyGameStat(gameId.value!!, isHome.value ?: false)
            myStat.value = when (result) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
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