package com.gillian.baseball.finalgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.EventType
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class FinalViewModel(private val repository: BaseballRepository, val myGame: MyGame) : ViewModel() {

    // 1. 更新box               uploadBox()
    // 2. 更新球員成績          getMyStat() -> observe -> upload hit and pitch
    // 如果我要讓使用者能確認資料，(調整打點資訊等) 可能就要按下按鈕才上船
    // 按下按鈕 -> 上傳 -> 上傳完才navigate

    // 可能要改成
    // 1. 更新球員成績 getMyStat() (抓到所有event -> toMyStat) -> observe -> upload hit and pitch
    // 2. update status
    // 3. 如果有note 才update

    // 我有修改過update Game Box 原本長這樣
    //        FirebaseFirestore.getInstance().collection(GAMES)
    //                .document(gameId)
    //                .update("box", box,
    //                        "status", GameStatus.FINAL.number)


    // get my stat ( including my hitters and my pitchers ) -> for loop update players boxes

    val box = myGame.game.box
    lateinit var gameResult: GameResult

    //var boxViewList = mutableListOf<BoxView>()
    val myStat = MutableLiveData<MyStatistic>()

    private val _saveAndNavigate = MutableLiveData<Boolean>()

    val saveAndNavigate: LiveData<Boolean>
        get() = _saveAndNavigate

    lateinit var hitterResult: Result<Boolean>
    lateinit var pitcherResult: Result<Boolean>

    val gameNote = MutableLiveData<String>(myGame.game.note)


    private val _gameBox = MutableLiveData<List<BoxView>>()
    val gameBox: LiveData<List<BoxView>>
        get() = _gameBox

    val loading = MutableLiveData<Boolean>(true)

    private val _statusNavigate = MutableLiveData<LoadStatus>()

    val statusNavigate: LiveData<LoadStatus>
        get() = _statusNavigate

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _eraChanged = MutableLiveData<Boolean>()

    val eraChanged: LiveData<Boolean>
        get() = _eraChanged

    init {
        // 需要先update status = final嗎
        createBoxViewList()
        fetchMyStat()
    }


    fun fetchMyStat() {
        viewModelScope.launch {

            _status.value = LoadStatus.LOADING
            loading.value = true

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

    fun updatePlayerStat() {
        myStat.value?.let {
            viewModelScope.launch {
                for (hitter in it.myHitter) {
                    if (hitter.playerId != "") {
                        hitterResult = repository.updateHitStat(hitter.playerId, hitter)
                    }
                }

                // TODO()不應該現在update
//                for (pitcher in it.myPitcher) {
//                    if (pitcher.playerId != "") {
//                        pitcherResult = repository.updatePitchStat(pitcher.playerId, pitcher)
//                    }
//                }

                Log.i("gillian12", "pitcher ${it.myPitcher}")
                updateGameStatus(GameStatus.FINALWITHSTAT)
                _status.value = LoadStatus.DONE
                loading.value = false
            }
        }
    }

    fun updateGameStatus(status: GameStatus) {
        viewModelScope.launch {
            repository.updateGameStatus(myGame.game.id, status)
        }
    }

    fun createBoxViewList() {
        _gameBox.value = box.toBoxViewList()
    }


    fun modifyPitcherEr(pitcher: PitcherBox, amount: Int) {
        Log.i("gillian12", "add")
        pitcher.earnedRuns += amount
        _eraChanged.value = true
    }

    fun onEraChanged() {
        _eraChanged.value = null
    }


    fun updateStatAndNote() {
        _statusNavigate.value = LoadStatus.LOADING
        viewModelScope.launch {

            myStat.value?.let{
                for (pitcher in it.myPitcher) {
                    if (pitcher.playerId != "") {
                        Log.i("gillian", "pitcher era is ${pitcher.earnedRuns}")
                        pitcherResult = repository.updatePitchStat(pitcher.playerId, pitcher)
                        repository.sendEvent(myGame.game.id,
                            Event(result = EventType.IPUPDATE.number,
                                out = pitcher.earnedRuns,
                                inning = if (myGame.isHome) 1 else 2,
                                pitcher = EventPlayer(playerId = pitcher.playerId, name = pitcher.name?:"", order = pitcher.order))
                        )
                    }
                }
            }

            val noteResult = repository.updateGameNote(myGame.game.id, gameNote.value!!)
            _saveAndNavigate.value = when (noteResult) {
                is Result.Success -> {
                    _statusNavigate.value = LoadStatus.DONE
                    noteResult.data
                }
                is Result.Fail -> {
                    _errorMessage.value = noteResult.error
                    _statusNavigate.value = LoadStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = noteResult.exception.toString()
                    _statusNavigate.value = LoadStatus.ERROR
                    null
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    _statusNavigate.value = LoadStatus.ERROR
                    null
                }
            }
        }
    }

    fun onTeamNavigated() {
        _saveAndNavigate.value = null
    }

}