package com.gillian.baseball.statistics.statgame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class StatGameViewModel(private val repository: BaseballRepository) : ViewModel() {

    val oneGameHitterStat = MutableLiveData<List<HitterBox>>()

    var gameId = MutableLiveData<String>()

    fun getHitterStat() {
        viewModelScope.launch {
            val result = repository.getHittersStat( gameId.value!! , "")
            oneGameHitterStat.value = when (result) {
                is Result.Success -> {
                    Log.i("gillian", "success")
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