package com.gillian.baseball.finalgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class FinalViewModel(private val repository: BaseballRepository, private val myGame: MyGame) : ViewModel() {

    // 1. 更新box
    // 2. 更新每個球員的成績
    // 3. ->所以我的stat要有player id有!我好棒XD
    // 4. -> 看過所有的stat的player id 一個一個執行repository上傳
    // 拿到所有event -> 算完得到boxex -> 回傳


    // get my stat ( including my hitters and my pitchers ) -> for loop update players boxes

    var box = myGame.game.box
    lateinit var gameResult : GameResult
    //var boxViewList = mutableListOf<BoxView>()
    val myStat = MutableLiveData<MyStatistic>()

    lateinit var hitterResult : Result<Boolean>
    lateinit var pitcherResult : Result<Boolean>

    val gameNote = MutableLiveData<String>()


    private val _submitAdapter = MutableLiveData<List<BoxView>>()
    val submitAdapter : LiveData<List<BoxView>>
        get() = _submitAdapter

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        uploadBox()
        createBoxViewList()
        getMyStat()
    }

    fun getMyStat() {
        viewModelScope.launch {

            _status.value = LoadStatus.LOADING

            val result = repository.getMyGameStat(myGame.game.id, myGame.isHome)
            myStat.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
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

    fun updateHitStat() {
        myStat.value?.let{
            viewModelScope.launch {
                for (hitter in it.myHitter) {
                    if (hitter.playerId != "") {
                        hitterResult = repository.updateHitStat(hitter.playerId, hitter)
                    }
                }
                _status.value = LoadStatus.DONE
            }
        }
    }

    fun updatePitchStat() {
        myStat.value?.let {
            viewModelScope.launch {
                for (pitcher in it.myPitcher) {
                    if (pitcher.playerId != "") {
                        pitcherResult = repository.updatePitchStat(pitcher.playerId, pitcher)
                    }
                }
            }
        }
    }

    fun createBoxViewList() {
        _submitAdapter.value = box.toBoxViewList()
    }

    fun uploadBox () {
        Log.i("gillian", "box is $box")
        viewModelScope.launch {
            val result = repository.updateGameBox(myGame.game.id, box)
            when (result) {
                is Result.Success -> {
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                }
            }
        }
    }



}