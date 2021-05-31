package com.gillian.baseball.loginflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch


class LoginSearchViewModel(private val repository: BaseballRepository) : ViewModel() {


    val searchTeamName = MutableLiveData<String>()

    val teamsList = MutableLiveData<List<Team>>()


    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToCreate = MutableLiveData<Boolean>()

    val navigateToCreate: LiveData<Boolean>
        get() = _navigateToCreate


    fun searchTeam() {
        if (searchTeamName.value.isNullOrEmpty()) {
            teamsList.value = emptyList()

        } else {
            viewModelScope.launch {
                val result = repository.searchTeam(searchTeamName.value!!)
                teamsList.value = when(result) {
                    is Result.Success -> {
                        _errorMessage.value = null
                        _status.value = LoadStatus.DONE
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
    }

    fun navigateToCreate() {
        _navigateToCreate.value = true
    }

    fun onCreateNavigated() {
        _navigateToCreate.value = null
    }

}