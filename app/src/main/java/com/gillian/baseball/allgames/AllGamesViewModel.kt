package com.gillian.baseball.allgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class AllGamesViewModel(private val repository: BaseballRepository) : ViewModel() {


    private val _navigateToOrder = MutableLiveData<Boolean>()
    val navigateToOrder : LiveData<Boolean>
        get() = _navigateToOrder

    private val _navigateToNewGame = MutableLiveData<Boolean>()
    val navigateToNewGame : LiveData<Boolean>
        get() = _navigateToNewGame


    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _myTeam = MutableLiveData<Team>()
    val myTeam : LiveData<Team>
        get() = _myTeam

    init {
        getMyTeam()
    }


    fun getMyTeam() {
        viewModelScope.launch {
            val result = repository.getTeam2(UserManager.teamId)

            _myTeam.value = when(result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    result.data
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    null
                }
                else -> {
                    _errorMessage.value = BaseballApplication.instance.getString(R.string.return_nothing)
                    null
                }
            }
        }
    }


    fun startANewGame() {
        //TODO()這邊應該要傳個東西過去
        _navigateToOrder.value = true
    }

    fun onNewGameStarted() {
        _navigateToOrder.value = null
    }

    fun createNewGame() {
        _navigateToNewGame.value = true
    }

    fun onNewGameCreated() {
        _navigateToNewGame.value = null
    }


}