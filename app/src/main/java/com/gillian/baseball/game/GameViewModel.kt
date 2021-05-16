package com.gillian.baseball.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class GameViewModel(private val repository: BaseballRepository, private val argument: Game) : ViewModel() {

    private val _game = MutableLiveData<Game>(argument)

    val game: LiveData<Game>
        get() = _game

    var homeRun = MutableLiveData<Int>(0)
    var guestRun = MutableLiveData<Int>(0)

    var inningCount = 1
    private var isTop = true

    val homeLineUp = argument.home.lineUp
    val guestLineUp = argument.guest.lineUp

    // 初始化lineup是客隊先攻
    var lineUp = listOf(EventPlayer())
    init {
        lineUp = guestLineUp
    }


    /* ------------------------ 打擊相關 ------------------------*/
    var ballCount = MutableLiveData<Int>(0)
    var strikeCount = MutableLiveData<Int>(0)
    var totalStrike = 0
    var outCount = MutableLiveData<Int>(0)


    fun homeScored() {
        homeRun.value = homeRun.value!!.plus(1)

    }

    fun guestScored() {
        guestRun.value = guestRun.value!!.plus(1)
    }

    fun switch() {
        clearCount()
        inningCount += 1
        outCount.value = 0

        if (isTop) {
            guestABNumber = atBatNumber  // 記錄下這次打席的最後人次
            atBatNumber = homeABNumber   // 開始下一局的人次
            lineUp = homeLineUp          // 下半局換主隊進攻
        } else {
            homeABNumber = atBatNumber
            atBatNumber = guestABNumber
            lineUp = guestLineUp
        }
        isTop = !isTop
        baseList = arrayOf(lineUp[atBatNumber], null, null, null)
        atBatName.value = "第${atBatNumber+1}棒 ${baseList[0]?.name}"
        Log.i("at base", "*-------------change!------------*")
        Log.i("at base","目前客隊打到第$guestABNumber 主隊打到第$homeABNumber")
        Log.i("at base", "下一隊的打者 $homeLineUp")
        updateRunnerUI()
        // clearBase?
    }


    // 進到dialog的時候帶過去的用球數
    lateinit var hitterEvent : Event

    var baseList = arrayOf<EventPlayer?>(lineUp[0], null, null, null)
    var atBaseList = mutableListOf<AtBase>()


    var homeABNumber = 0
    var guestABNumber = 0

    var atBatNumber = 0
    var atBatName = MutableLiveData<String>().apply{
        value = "第1棒 ${baseList[0]?.name}"
    }

    private val _navigateToEvent = MutableLiveData<List<AtBase>>()
    val navigateToEvent : LiveData<List<AtBase>>
        get() = _navigateToEvent

    private val _navigateToOnBase = MutableLiveData<Int>()
    val navigateToOnBase : LiveData<Int>
        get() = _navigateToOnBase

    private val _navigateToOut = MutableLiveData<List<AtBase>>()
    val navigateToOut : LiveData<List<AtBase>>
        get() = _navigateToOut


    var firstBaseVisible = MutableLiveData<Boolean>(false)
    var firstBaseName = MutableLiveData<String>("")
    var secondBaseVisible = MutableLiveData<Boolean>(false)
    var secondBaseName = MutableLiveData<String>("")
    var thirdBaseVisible = MutableLiveData<Boolean>(false)
    var thirdBaseName = MutableLiveData<String>("")


    fun ball() {
        ballCount.value = ballCount.value!!.plus(1)
        if (ballCount.value == 4) {
            // single
            ballFour()
        }
    }

    fun strike() {
        strikeCount.value = strikeCount.value!!.plus(1)
        totalStrike += 1

        if (strikeCount.value == 3) {
            sendEvent(Event(player = lineUp[atBatNumber],
                    result = 9,
                    ball = ballCount.value ?: 0,
                    strike = totalStrike,
                    out = 1))
            out()
        }
    }

    fun foul() {
        if (strikeCount.value!! < 2) {
            strikeCount.value = strikeCount.value!!.plus(1)
        }
        totalStrike += 1
    }

    fun out() {
        outCount.value = outCount.value!!.plus(1)
        if (outCount.value!! == 3) {
            // three out! switch TODO()這邊跟next player的差別要處理一下
            if (atBatNumber == (lineUp.size-1)) {
                atBatNumber = 0
            } else {
                atBatNumber += 1
            }
            switch()
        } else {
            nextPlayer()
        }
    }

    fun onBaseOut(base: Int) {
        outCount.value = outCount.value!!.plus(1)
        if (outCount.value!! == 3) {
            // three out! switch
            outCount.value = 0
            Log.i("at base", "*-------------change!------------*")
        } else {
            baseList[base] = null
        }
    }

    fun toEventDialog(isSafe: Boolean) {
        atBaseList.clear()
        hitterEvent = Event(
                ball = ballCount.value ?: 0,
                strike = totalStrike,
                player = lineUp[atBatNumber]
        )

        baseList[0] = lineUp[atBatNumber]
        for ((index, player) in baseList.withIndex()) {
            if (player != null) {
                atBaseList.add(AtBase(base = index, player = player))
            }
        }

        if (isSafe) {
            _navigateToEvent.value = atBaseList
        } else {
            if (outCount.value == 2) {
                _navigateToOut.value = listOf(AtBase(0, baseList[0]!!))
            } else {
                _navigateToOut.value = atBaseList
            }
        }
    }

    fun showOnBaseDialog(base: Int) {
        _navigateToOnBase.value = base
    }

    fun onEventNavigated() {
        _navigateToEvent.value = null
    }

    fun onOutNavigated() {
        _navigateToOut.value = null
    }

    fun onOnBaseNavigated() {
        _navigateToOnBase.value = null
    }


    fun advanceBase(myself: Int) {
        var end = myself
        while ( baseList[end] != null) {
            if (end == 3) {
                // 擠壘事件造成的得分
                sendEvent(Event(player = baseList[3]!!, run = 1))
                break
            }
            end += 1
        }
        for ( i in end downTo (myself+1)) {
            baseList[i] = baseList[i-1]
        }
        baseList[myself] = null

//        // debugging
//        Log.i("at base ", "--------------壘上事件變化啦-------------")
//        for ((index, base) in baseList.withIndex()){
//            Log.i("at base advance", "base $index is now ${base?.name}")
//        }
//        Log.i("at base ", "--------------壘上事件結束-------------")
        updateRunnerUI() // 放這邊合理嗎?
    }

    fun ballFour() {
        sendEvent(Event(player = lineUp[atBatNumber],
                result = 8,
                ball = 4,
                strike = totalStrike,
                out = outCount.value ?: 0))
        advanceBase(0)
        clearCount()
        nextPlayer()
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            repository.sendEvent(event)
        }
    }

    fun nextPlayer() {
        clearCount()
        // if current at bat is the last player of line up
        if (atBatNumber == (lineUp.size-1)) {
            atBatNumber = 0
        } else {
            atBatNumber += 1
        }

        // change the hitter to next person in line
        baseList[0] = lineUp[atBatNumber]
        atBatName.value = "第${atBatNumber+1}棒 ${baseList[0]!!.name}"
        updateRunnerUI()
    }

    fun updateRunnerUI() {
        if (baseList[1] == null) {
            firstBaseVisible.value = false
        } else {
            firstBaseVisible.value = true
            firstBaseName.value = baseList[1]!!.name
        }
        if (baseList[2] == null) {
            secondBaseVisible.value = false
        } else {
            secondBaseVisible.value = true
            secondBaseName.value = baseList[2]!!.name
        }

        if (baseList[3] == null) {
            thirdBaseVisible.value = false
        } else {
            thirdBaseVisible.value = true
            thirdBaseName.value = baseList[3]!!.name
        }
    }

    fun setNewBaseList(newList: Array<EventPlayer?>) {
        baseList = newList
    }

    fun clearCount() {
        totalStrike = 0
        ballCount.value = 0
        strikeCount.value = 0
    }

    fun clearBase() {
        baseList = arrayOf<EventPlayer?>(null, null, null, null)
    }



}