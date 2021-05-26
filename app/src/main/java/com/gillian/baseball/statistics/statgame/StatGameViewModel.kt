package com.gillian.baseball.statistics.statgame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class StatGameViewModel(private val repository: BaseballRepository) : ViewModel() {

    val allStat = MutableLiveData<Statistic>()

    val myStat = MutableLiveData<MyStatistic>()

    val gameBox = MutableLiveData<List<BoxView>>()

    var gameId = MutableLiveData<String>()
    var isHome = MutableLiveData<Boolean>()

    fun getGameBox() {
        viewModelScope.launch {
            val boxResult = repository.getGameBox(gameId.value!!)
            gameBox.value = when (boxResult) {
                is Result.Success -> {
                    Log.i("gillian", "game box")
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


//    fun getAllStat() {
//        viewModelScope.launch {
//            val result = repository.getGameStat(gameId.value!!, UserManager.teamId)
//            allStat.value = when (result) {
//                is Result.Success -> {
//                    Log.i("gillian", "all stat")
//                    result.data
//                }
//                is Result.Fail -> {
//                    null
//                }
//                is Result.Error -> {
//                    null
//                }
//                else -> {
//                    null
//                }
//            }
//        }
//    }


    fun getMyTeamStat() {
        viewModelScope.launch {
            val result = repository.getMyGameStat(gameId.value!!, isHome.value ?: false)
            myStat.value = when (result) {
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


}