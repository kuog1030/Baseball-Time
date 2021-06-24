package com.gillian.baseball.allbroadcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.GameCard
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class AllBroadcastViewModel(private val repository: BaseballRepository) : ViewModel() {


    val searchTitle = MutableLiveData<String>()

    // cache the games data for title searching
    var cacheAllGames: List<GameCard> = emptyList()

    private val _allLiveResult = MutableLiveData<List<GameCard>>()

    val allLiveResult: LiveData<List<GameCard>>
        get() = _allLiveResult

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    init {
        fetchAllLiveGame(true)
    }

    private fun fetchAllLiveGame(isInit: Boolean = false) {
        viewModelScope.launch {
            if (isInit) _status.value = LoadStatus.LOADING
            val result = repository.getAllLiveGamesCard()

            _allLiveResult.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = checkIfNoGame(result.data.size)
                    if (isInit) _status.value = LoadStatus.DONE
                    cacheAllGames = result.data
                    result.data
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    if (isInit) _status.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    if (isInit) _status.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    if (isInit) _status.value = LoadStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun searchGame() {
        if (searchTitle.value == null) {
            _allLiveResult.value = cacheAllGames

        } else {
            val resultList = cacheAllGames.filter { card -> (card.title).contains(searchTitle.value!!) }

            _errorMessage.value = if (resultList.isEmpty()) Util.getString(R.string.error_search) else null
            _allLiveResult.value = resultList
        }
    }

    private fun checkIfNoGame(size: Int): String? {
        return if (size == 0) {
            Util.getString(R.string.error_no_live_game)
        } else {
            null
        }
    }

    fun refresh() {
        if (_status.value != LoadStatus.LOADING) {
            fetchAllLiveGame(false)
        }
    }

}