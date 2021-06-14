package com.gillian.baseball.statistics.statplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class StatPlayerViewModel(private val repository: BaseballRepository) : ViewModel() {

    val player = MutableLiveData<Player>()

    val editable = MutableLiveData<Boolean>(false)

    val noUserRegistered = MutableLiveData<Boolean>(false)

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToEdit = MutableLiveData<Player>()

    val navigateToEdit: LiveData<Player>
        get() = _navigateToEdit

    val infoVisibility = MutableLiveData<Boolean>(false)

    private val _navigateToTeam = MutableLiveData<Boolean>()

    val navigateToTeam: LiveData<Boolean>
        get() = _navigateToTeam

    var isInit = true



    fun fetchPlayerStat(playerId: String) {
        Log.i("gillian", "get player stat!!")
        viewModelScope.launch {
            val result = repository.getOnePlayer(playerId)

            player.value = when(result) {
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
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    fun updateMoreHitStat() {
        player.value?.let{
            editable.value = (it.userId.isNullOrEmpty() || it.userId == UserManager.userId)  // 如果這個球員沒人認領 user id is null才可以修改
            noUserRegistered.value = it.userId.isNullOrEmpty()
        }
    }

    fun navigateToEdit() {
        _navigateToEdit.value = player.value
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
    }

    fun showInfo(show: Boolean = true) {
        if (show) {
            infoVisibility.value = !(infoVisibility.value!!)
        } else {
            infoVisibility.value = false
        }

    }

    fun navigateToTeam() {
        _navigateToTeam.value = true
    }

    fun onTeamNavigated() {
        _navigateToTeam.value = null
    }

    fun refresh(refreshStat: Boolean = false) {
        isInit = refreshStat
        player.value?.let {
            Log.i("gillian", "refresh")
            fetchPlayerStat(it.id)
        }
    }

//    fun createBox() {
//        // only triggered when player value is not null
//        val hitListWithTitle = mutableListOf(HitterBox())
//        hitListWithTitle.add(player.value!!.hitStat)
//        val pitchListWithTitle = mutableListOf(PitcherBox())
//        pitchListWithTitle.add(player.value!!.pitchStat)
//
//        hitStat.value = hitListWithTitle
//        pitchStat.value = pitchListWithTitle
//        Log.i("gillian", "hit stat ${hitStat.value!!.size} and ${hitStat.value!![1]}")
//    }

}