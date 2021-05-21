package com.gillian.baseball.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.data.EventInfo
import kotlinx.coroutines.launch

// debug用
val totalInning = 6

class GameViewModel(private val repository: BaseballRepository, private val argument: MyGame) : ViewModel() {


    /* ---------------------------- init game info ----------------------------

       1. 兩邊打擊順序
       2. 兩邊投手
       3. 兩邊分數歸零
       4. 局數從1開始
       5. 使用者是主隊or客隊
     --------------------------------------------------------------------------- */

    private val _game = MutableLiveData<Game>(argument.game)

    val game: LiveData<Game>
        get() = _game

    var homeRun = MutableLiveData<Int>(0)
    var guestRun = MutableLiveData<Int>(0)

    var inningCount = 1
    var inningShow = MutableLiveData<String>("1上")

    val isHome = argument.isHome ?: true
    var isTop = true

    val homeLineUp = argument.game.home.lineUp
    val guestLineUp = argument.game.guest.lineUp

    // 替補球員假資料
    var myBench = mutableListOf(EventPlayer(name = "Gillian", number = "22"),
            EventPlayer(name = "Wency", number = "30"),
            EventPlayer(name = "Chloe", number = "2"))

    // 開賽由主隊投手先投
    val pitcher = MutableLiveData<String>(argument.game.home.pitcher.name)
    var homePitcher = argument.game.home.pitcher
    var guestPitcher = argument.game.guest.pitcher

    // 初始化lineup是客隊先攻
    var lineUp = mutableListOf(EventPlayer())

    init {
        lineUp = guestLineUp
    }

    /* ---------------------------- init game info ---------------------------- */


    /* ---------------------------- 打擊相關 ----------------------------*/
    var ballCount = MutableLiveData<Int>(0)
    var strikeCount = MutableLiveData<Int>(0)
    var totalStrike = 0
    var outCount = MutableLiveData<Int>(0)


    fun scored(score: Int) {
        if (isTop) {
            guestRun.value = guestRun.value!!.plus(score)
        } else {
            homeRun.value = homeRun.value!!.plus(score)
        }
        game.value?.let {
            it.box.score[inningCount - 1] += score
            it.box.run[(inningCount - 1) % 2] += score
        }
    }

    fun switch() {

        if (inningCount == totalInning) {
            _navigateToFinal.value = MyGame(isHome = isHome, game = game.value!!)
        } else {
            if (atBatNumber == (lineUp.size - 1)) {
                atBatNumber = 0
            } else {
                atBatNumber += 1
            }

            clearCount()
            outCount.value = 0
            inningCount += 1
            if (isTop) {
                // 目前上半局
                guestABNumber = atBatNumber  // 記錄下這次打席的最後人次+1
                atBatNumber = homeABNumber   // 開始下一局的人次
                lineUp = homeLineUp          // 下半局換主隊進攻
                pitcher.value = guestPitcher.name
                inningShow.value = "${(inningCount / 2)} 下"
            } else {
                homeABNumber = atBatNumber
                atBatNumber = guestABNumber
                lineUp = guestLineUp
                pitcher.value = homePitcher.name
                inningShow.value = "${(inningCount / 2) + 1} 上"
            }


            Log.i("at base", "box score ${game.value!!.box.score}")
            Log.i("at base", "hit box ${game.value!!.box.hit}")
            Log.i("at base", "run box ${game.value!!.box.run}")

            game.value!!.box.score.add(0)
            isTop = !isTop
            baseList = arrayOf(lineUp[atBatNumber], null, null, null)
            atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]?.name}"
            Log.i("at base", "*-------------change!------------*")
            updateRunnerUI()
            // clearBase?
        }
    }


    // 進到dialog的時候帶過去的用球數
    lateinit var hitterEvent: Event

    var baseList = arrayOf<EventPlayer?>(lineUp[0], null, null, null)
    var atBaseList = mutableListOf<AtBase>()


    var homeABNumber = 0
    var guestABNumber = 0

    var atBatNumber = 0
    var atBatName = MutableLiveData<String>().apply {
        value = "第1棒 ${baseList[0]?.name}"
    }

    private val _navigateToEvent = MutableLiveData<EventInfo>()
    val navigateToEvent: LiveData<EventInfo>
        get() = _navigateToEvent

    private val _navigateToOnBase = MutableLiveData<OnBaseInfo>()
    val navigateToOnBase: LiveData<OnBaseInfo>
        get() = _navigateToOnBase

    private val _navigateToOut = MutableLiveData<EventInfo>()
    val navigateToOut: LiveData<EventInfo>
        get() = _navigateToOut

    private val _navigateToFinal = MutableLiveData<MyGame>()
    val navigateToFinal: LiveData<MyGame>
        get() = _navigateToFinal

    private val _navigateToPinch = MutableLiveData<Boolean>()
    val navigateToPinch: LiveData<Boolean>
        get() = _navigateToPinch


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
                    pitcher = if (isTop) homePitcher else guestPitcher,
                    inning = inningCount,
                    result = EventType.STRIKEOUT.number,
                    ball = ballCount.value ?: 0,
                    strike = totalStrike,
                    out = outCount.value ?: 0))
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
            switch()
        } else {
            nextPlayer()
        }
    }

    fun onBaseOut(base: Int) {
        sendEvent(Event(
                inning = inningCount,
                out = outCount.value ?: 0,
                player = baseList[base]!!,
                result = EventType.PICKOFF.number,
                pitcher = if (isTop) homePitcher else guestPitcher
        ))
        outCount.value = outCount.value!!.plus(1)
        if (outCount.value!! == 3) {
            // three out! switch
            switch()
        } else {
            baseList[base] = null
        }
        updateRunnerUI()
    }

    fun toEventDialog(isSafe: Boolean) {
        atBaseList.clear()
        hitterEvent = Event(
                inning = inningCount,
                ball = ballCount.value ?: 0,
                strike = totalStrike,
                out = outCount.value ?: 0,
                player = lineUp[atBatNumber],
                pitcher = if (isTop) homePitcher else guestPitcher
        )

        baseList[0] = lineUp[atBatNumber]
        for ((index, player) in baseList.withIndex()) {
            if (player != null) {
                atBaseList.add(AtBase(base = index, player = player))
            }
        }

        if (isSafe) {
            _navigateToEvent.value = EventInfo(gameId = argument.game.id,
                    atBaseList = atBaseList,
                    isSafe = true,
                    hitterPreEvent = hitterEvent)
        } else {

            // 如果兩出局，只管打者
            if (outCount.value == 2) {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = listOf(AtBase(0, baseList[0]!!)),
                        isSafe = false,
                        hitterPreEvent = hitterEvent)

            } else {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = atBaseList,
                        isSafe = false,
                        hitterPreEvent = hitterEvent)
            }
        }
    }

    fun toOnBaseDialog(base: Int) {
        _navigateToOnBase.value = OnBaseInfo(
                gameId = argument.game.id,
                inning = inningCount,
                out = outCount.value ?: 0,
                onClickPlayer = base,
                pitcher = if (isTop) homePitcher else guestPitcher,
                baseList = baseList.toList()
        )
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

    fun onFinalNavigated() {
        _navigateToFinal.value = null
    }

    fun onPinchNavigated() {
        _navigateToPinch.value = null
    }


    fun advanceBase(myself: Int) {
        var end = myself
        while (baseList[end] != null) {
            if (end == 3) {
                // 擠壘事件造成的得分
                sendEvent(Event(player = baseList[3]!!,
                        pitcher = if (isTop) homePitcher else guestPitcher,
                        run = 1,
                        result = EventType.RUN.number,
                        out = outCount.value ?: 0,
                        inning = inningCount))
                scored(1)
                break
            }
            end += 1
        }
        for (i in end downTo (myself + 1)) {
            baseList[i] = baseList[i - 1]
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
                pitcher = if (isTop) homePitcher else guestPitcher,
                inning = inningCount,
                result = EventType.WALK.number,
                ball = 4,
                strike = totalStrike,
                out = outCount.value ?: 0))
        advanceBase(0)
        clearCount()
        nextPlayer()
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            repository.sendEvent(argument.game.id, event)
        }
    }

    fun nextPlayer() {
        clearCount()
        // if current at bat is the last player of line up
        if (atBatNumber == (lineUp.size - 1)) {
            atBatNumber = 0
        } else {
            atBatNumber += 1
        }

        // change the hitter to next person in line
        baseList[0] = lineUp[atBatNumber]
        atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]!!.name}"
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

    fun addHitToBox(run: Int) {
        game.value?.let {
            if (isTop) {
                it.box.hit[0] += run
            } else {
                it.box.hit[1] += run
            }
        }
    }

    // 換代打
    fun navigateToPinch() {
        _navigateToPinch.value = true
    }

    fun pinch(player: EventPlayer, position: Int) {
        player.pinch = lineUp[atBatNumber]
        if (isTop) {
            guestLineUp[atBatNumber] = player
            Log.i("gillian", "now guest line up $guestLineUp")
        } else {
            homeLineUp[atBatNumber] = player
            Log.i("gillian", "now home line up $homeLineUp")
        }
        lineUp[atBatNumber] = player
        atBatName.value = player.name

        myBench.removeAt(position)

        Log.i("gillian", "移除掉替補球員，應該要剩下 $myBench")
    }

    fun nextPitcher(next: EventPlayer, position: Int) {
        if (isTop) {
            next.pinch = homePitcher
            homePitcher = next
        } else {
            next.pinch = guestPitcher
            guestPitcher = next
        }
        pitcher.value = next.name
        myBench.removeAt(position)
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

/*
    暫時當作optional 換人、失誤的部分

    fun onOptionNavigated() {
        _navigateToOption.value = null
    }


    private val _navigateToOption = MutableLiveData<List<EventPlayer>>()
    val navigateToOption: LiveData<List<EventPlayer>>
        get() = _navigateToOption
 */