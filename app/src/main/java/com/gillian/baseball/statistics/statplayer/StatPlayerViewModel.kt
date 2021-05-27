package com.gillian.baseball.statistics.statplayer

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.PitcherBox
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class StatPlayerViewModel(private val repository: BaseballRepository) : ViewModel() {

    val player = MutableLiveData<Player>()

    val editable = MutableLiveData<Boolean>(false)

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigateToEdit = MutableLiveData<Player>()

    val navigateToEdit: LiveData<Player>
        get() = _navigateToEdit


    val myAvg = MutableLiveData<String>()

    val myObp = MutableLiveData<String>()

    val mySlg = MutableLiveData<String>()



    fun getPlayerStat(playerId: String) {
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
        val statFormat = "%.3f"
        player.value?.let{

            editable.value = (it.userId.isNullOrEmpty())  // 如果這個球員沒人認領 user id is null才可以修改

            myAvg.value = statFormat.format(it.hitStat.myAverage() ?: 0F)
            myObp.value = statFormat.format(it.hitStat.myObp() ?: 0F)
            mySlg.value = statFormat.format(it.hitStat.mySlg() ?: 0F)
        }
    }

    fun navigateToEdit() {
        _navigateToEdit.value = player.value
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
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