package com.gillian.baseball.allbroadcast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class AllBroadcastViewModel(private val repository: BaseballRepository) : ViewModel() {


    private val _allLiveResult = MutableLiveData<List<GameCard>>()

    val allLiveResult: LiveData<List<GameCard>>
        get() = _allLiveResult

    val searchTitle = MutableLiveData<String>()


    var cacheAllGames: List<GameCard> = emptyList()

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage


    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status


    init {
        getAllLiveGame(true)
    }

    fun getAllLiveGame(isInit: Boolean = false) {
        viewModelScope.launch {
            _status.value = LoadStatus.LOADING
            val result = repository.getAllLiveGamesCard()

            _allLiveResult.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    _status.value = LoadStatus.DONE
                    if (isInit) cacheAllGames = result.data
                    if (result.data.isEmpty()) noLiveGame()
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
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    _status.value = LoadStatus.ERROR
                    null
                }
            }
        }

    }

    // 我需要設定unclickable欸
    fun searchGame() {
        Log.i("gillian", "searching ${searchTitle.value}")

        if (searchTitle.value == null) {
            _allLiveResult.value = cacheAllGames

        } else {
            val resultList = cacheAllGames.filter { card -> (card.title).contains(searchTitle.value!!) }

            _errorMessage.value = if (resultList.isEmpty()) Util.getString(R.string.error_search) else null
            _allLiveResult.value = resultList
        }
    }

    fun noLiveGame() {
        _errorMessage.value = Util.getString(R.string.error_no_live_game)
    }

}