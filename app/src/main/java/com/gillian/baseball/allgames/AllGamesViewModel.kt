package com.gillian.baseball.allgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.source.BaseballRepository

class AllGamesViewModel(private val repository: BaseballRepository) : ViewModel() {


    private val _navigateToOrder = MutableLiveData<Boolean>()
    val navigateToOrder : LiveData<Boolean>
        get() = _navigateToOrder


    fun startANewGame() {
        //TODO()這邊應該要傳個東西過去
        _navigateToOrder.value = true
    }

    fun onNewGameStarted() {
        _navigateToOrder.value = null
    }


}