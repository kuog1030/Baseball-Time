package com.gillian.baseball.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.data.EventInfo
import com.gillian.baseball.ext.lineUpPlayer
import kotlinx.coroutines.launch

// debug用
var totalInning = 6

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

    val isHome = argument.isHome



    /* ------------------------------------------------------------------------
           主客隊的資訊
    --------------------------------------------------------------------------- */

    var homeRun = MutableLiveData<Int>(0)
    var guestRun = MutableLiveData<Int>(0)

    val homeLineUp = argument.game.home.lineUp
    val guestLineUp = argument.game.guest.lineUp

    var homePitcher = argument.game.home.pitcher
    var guestPitcher = argument.game.guest.pitcher

    var homePitchCount = 0
    var guestPitchCount = 0

    var homeABNumber = 0
    var guestABNumber = 0

    var myBench = argument.benchPlayer

    /* ------------------------------------------------------------------------
                    兩隊共同使用的東西，會被更新
    --------------------------------------------------------------------------- */


    var isTop = true
    val liveIsTop = MutableLiveData<Boolean>(true)

    var inningCount = 1
    var inningShow = MutableLiveData<String>("1")

    //計算上一個投手的投球局數
    var previousIp = 0
    var pitcherCount = 1

    val liveBallCount = MutableLiveData<Int>(0)

    // 開賽由主隊投手先投，客隊打者先攻
    val pitcher = MutableLiveData<String>(argument.game.home.pitcher.name)
    var lineUp = mutableListOf(EventPlayer())

    var ballCount = MutableLiveData<Int>(0)
    var strikeCount = MutableLiveData<Int>(0)
    var outCount = MutableLiveData<Int>(0)
    var totalStrike = 0

    val nextAtBat = MutableLiveData<String>()

    init {
        lineUp = guestLineUp
        nextAtBat.value = lineUp.lineUpPlayer(1).name
    }
    // 進到dialog的時候帶過去的用球數
    lateinit var hitterEvent: Event

    var baseList = arrayOf<EventPlayer?>(lineUp[0], null, null, null)
    var atBaseList = mutableListOf<AtBase>()

    var atBatNumber = 0
    var atBatName = MutableLiveData<String>().apply {
        value = "第1棒 ${baseList[0]?.name}"
    }



    /* ------------------------------------------------------------------------
                                    init done
    --------------------------------------------------------------------------- */

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

        uploadGameBox()
    }

    fun uploadGameBox() {
        viewModelScope.launch {
            game.value?.let{
                repository.updateGameBox(it.id, it.box)
            }
        }
    }

    fun switch() {

        if (inningCount == totalInning) {
            game.value?.let{
                if (it.box.run[0] == it.box.run[1] && totalInning <= 12) {
                    // If Tie
                    totalInning += 2
                    switch()
                } else {
                    switchFinal()
                }
            }

        } else {
            // 比賽還沒結束
            if (atBatNumber == (lineUp.size - 1)) {
                atBatNumber = 0
            } else {
                atBatNumber += 1
            }

            clearCount(includeOut = true)
            inningCount += 1

            sendEvent(Event(
                inning = inningCount,
                result = EventType.INNINGCHANGE.number
            ))

            if (isTop) {
                // 目前上半局
                homePitchCount = liveBallCount.value!!  // 紀錄這次投球數
                liveBallCount.value = guestPitchCount

                guestABNumber = atBatNumber  // 記錄下這次打席的最後人次+1
                atBatNumber = homeABNumber   // 開始下一局的人次
                lineUp = homeLineUp          // 下半局換主隊進攻
                pitcher.value = guestPitcher.name
                inningShow.value = "${(inningCount / 2)}"
            } else {
                guestPitchCount += liveBallCount.value!!
                liveBallCount.value = homePitchCount

                homeABNumber = atBatNumber
                atBatNumber = guestABNumber
                lineUp = guestLineUp
                pitcher.value = homePitcher.name
                inningShow.value = "${(inningCount / 2) + 1}"
            }

            uploadGameBox()
            game.value!!.box.score.add(0)

            isTop = !isTop
            liveIsTop.value = isTop
            baseList = arrayOf(lineUp[atBatNumber], null, null, null)
            atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]?.name}"
            Log.i("at base", "*-------------change!------------*")
            updateRunnerUI()
            // clearBase?
        }
    }


    fun switchFinal() {

        val totalInningPitched = (((inningCount+1) / 2 - 1) * 3 + outCount.value!!) - previousIp
        Log.i("game", "換投中 event送達~, out count ${outCount.value}")

        sendEvent(
                Event(
                        pitcher = if (isHome) homePitcher else guestPitcher,
                        inning = if (isHome) (inningCount-1) else inningCount,
                        result = EventType.INNINGSPITCHED.number, //50
                        out = totalInningPitched
                )
        )

        uploadGameBox()
        _navigateToFinal.value = MyGame(isHome = isHome, game = game.value!!, benchPlayer = myBench)
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
        addPitchCount()
        ballCount.value = ballCount.value!!.plus(1)
        if (ballCount.value == 4) {
            // single
            ballFour()
        }
    }

    fun strike() {
        addPitchCount()
        strikeCount.value = strikeCount.value!!.plus(1)
        totalStrike += 1

        if (strikeCount.value == 3) {
            val tempOut = outCount.value ?: 0
            sendEvent(Event(player = lineUp[atBatNumber],
                    pitcher = if (isTop) homePitcher else guestPitcher,
                    inning = inningCount,
                    result = EventType.STRIKEOUT.number,
                    currentBase = toCustomBaseInt(baseList = baseList),
                    ball = ballCount.value ?: 0,
                    strike = totalStrike,
                    out = tempOut + 1))
            out()
        }
    }

    fun foul() {
        addPitchCount()
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

    fun eventOut(totalOut: Int) {
        outCount.value = outCount.value!!.plus(totalOut)
        if (outCount.value!! >= 3) {
            switch()
        } else {
            nextPlayer()
        }
    }

    fun addPitchCount() {
        liveBallCount.value = liveBallCount.value!!.plus(1)
    }


    // 1. 牽制出局 2. 盜壘失敗
    fun onBaseOut(base: Int, type: EventType) {
        outCount.value = outCount.value!!.plus(1)
        val tempPlayer = baseList[base]!!
        baseList[base] = null

        sendEvent(Event(
            inning = inningCount,
            out = outCount.value!!,
            player = tempPlayer,
            result = type.number,
            currentBase = toCustomBaseInt(baseList = baseList),
            pitcher = if (isTop) homePitcher else guestPitcher
        ))

        if (outCount.value!! == 3) {
            // three out! switch
            switch()
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
                    isDefence = (isTop == isHome),
                    hitterPreEvent = hitterEvent,
                    onField = if(isHome) homeLineUp else guestLineUp)
        } else {

            // 如果兩出局，只管打者
            if (outCount.value == 2) {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = mutableListOf(AtBase(0, baseList[0]!!)),
                        isSafe = false,
                        isDefence = (isTop == isHome),
                        hitterPreEvent = hitterEvent,
                        onField = if(isHome) homeLineUp else guestLineUp)

            } else {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = atBaseList,
                        isSafe = false,
                        isDefence = (isTop == isHome),
                        hitterPreEvent = hitterEvent,
                        onField = if(isHome) homeLineUp else guestLineUp)
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
                baseList = baseList.toList(),
                isDefence = (isTop == isHome)
        )
    }


    fun toCustomBaseInt(baseList: Array<EventPlayer?>) : Int {
        var result = 0
        for (i in 1..3) {
            if (baseList[i] != null) {
                when (i) {
                    1 -> result += 1
                    2 -> result += 10
                    3 -> result += 100
                }
            }
        }
        return result
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


    fun advanceBase(myself: Int) : Boolean {
        var hasRbi = false
        var end = myself
        while (baseList[end] != null) {
            if (end == 3) {
                // 擠壘事件造成的得分
                hasRbi = true
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
        return (hasRbi)
    }

    fun ballFour() {
        val toBeSend = Event(player = lineUp[atBatNumber],
                pitcher = if (isTop) homePitcher else guestPitcher,
                inning = inningCount,
                result = EventType.WALK.number,
                ball = 4,
                strike = totalStrike,
                out = outCount.value ?: 0)
        val hasRbi = advanceBase(0)
        toBeSend.currentBase = toCustomBaseInt(baseList = baseList)
        Log.i("gillian", "to be send ${toBeSend.currentBase}")

        if (hasRbi) {
            toBeSend.rbi = 1
        }

        sendEvent(toBeSend)
        clearCount(includeOut = false)
        nextPlayer()
    }

    fun sendEvent(event: Event) {
        Log.i("gillian", "game VM send event base ${event.currentBase}")
        viewModelScope.launch {
            repository.sendEvent(argument.game.id, event)
        }
    }

    fun nextPlayer() {
        clearCount(includeOut = false)
        // if current at bat is the last player of line up
        Log.i("gillian67", "current ${atBatNumber} and next ${atBatNumber}")

        if (atBatNumber == (lineUp.size - 1)) {
            atBatNumber = 0
        } else {
            atBatNumber += 1
        }

        Log.i("gillian67", "current ${atBatNumber} and next ${atBatNumber}")
        // change the hitter to next person in line
        baseList[0] = lineUp[atBatNumber]
        atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]!!.name}"

        nextAtBat.value = lineUp.lineUpPlayer(atBatNumber+1).name

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

    fun addErrorToBox(errorCount: Int) {
        game.value?.let {
            if (isTop) {
                it.box.error[1] += errorCount
            } else {
                it.box.error[0] += errorCount
            }
        }
        Log.i("gillian64", "current error box ${game.value!!.box}")
    }

    // 換代打
    fun navigateToPinch() {
        _navigateToPinch.value = true
    }

    fun pinch(player: EventPlayer, position: Int) {
        player.order = lineUp[atBatNumber].order + 1

        Log.i("gillian", "替補${player} 代替 ${guestLineUp[atBatNumber]}上場")
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


    // pinchDialog -> 換投
    fun nextPitcher(next: EventPlayer, position: Int) {
        val totalInningPitched = (((inningCount+1) / 2 - 1) * 3 + outCount.value!!) - previousIp
        previousIp = totalInningPitched

        Log.i("game", "換投中 event送達~")
        sendEvent(
            Event(
                pitcher = if (isTop) homePitcher else guestPitcher,
                inning = inningCount,
                result = EventType.INNINGSPITCHED.number,  // 50
                out = totalInningPitched
            )
        )

        // 第幾任投手
        pitcherCount += 1
        next.order = pitcherCount
        if (isTop) {
            homePitchCount = 0
            homePitcher = next
        } else {
            guestPitchCount = 0
            guestPitcher = next
        }
        liveBallCount.value = 0
        pitcher.value = next.name
        myBench.removeAt(position)
    }


    fun setNewBaseList(newList: Array<EventPlayer?>) {
        baseList = newList
    }

    fun clearCount(includeOut: Boolean) {
        totalStrike = 0
        ballCount.value = 0
        strikeCount.value = 0
        if (includeOut) outCount.value = 0
    }

    fun clearBaseList() {
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