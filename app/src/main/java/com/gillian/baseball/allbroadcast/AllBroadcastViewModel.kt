package com.gillian.baseball.allbroadcast

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

    val allLiveResult : LiveData<List<GameCard>>
        get() = _allLiveResult

    lateinit var cacheAllGames : List<GameCard>

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


    fun searchGame(title: String) {

    }

}