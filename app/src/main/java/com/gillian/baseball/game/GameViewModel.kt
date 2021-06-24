package com.gillian.baseball.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.ext.lineUpPlayer
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList


class GameViewModel(private val repository: BaseballRepository, private val argument: MyGame) : ViewModel() {

    private val _game = MutableLiveData<Game>(argument.game)

    val game: LiveData<Game>
        get() = _game

    val isHome = argument.isHome

    /* ------------------------------------------------------------------------
                            home and guest team info
    --------------------------------------------------------------------------- */


    val homeLineUp = argument.game.home.lineUp
    val guestLineUp = argument.game.guest.lineUp

    var homePitcher = argument.game.home.pitcher
    var guestPitcher = argument.game.guest.pitcher

    var homePitchCount = 0
    var guestPitchCount = 0

    var homeABNumber = 0
    var guestABNumber = 0

    var myBench = CopyOnWriteArrayList<EventPlayer>().apply {
        this.addAll(argument.benchPlayer)
    }

    /* ------------------------------------------------------------------------
              the current game info which will be updated every inning
    --------------------------------------------------------------------------- */

    var homeRun = MutableLiveData(0)
    var guestRun = MutableLiveData(0)

    val inningShow = MutableLiveData<String>("1")
    val liveIsTop = MutableLiveData<Boolean>(true)
    val liveBallCount = MutableLiveData(0)

    // the game start with home pitcher with guest hitter
    val pitcherName = MutableLiveData<String>(argument.game.home.pitcher.name)
    val nextAtBat = MutableLiveData<String>()

    val ballCount = MutableLiveData(0)
    val strikeCount = MutableLiveData(0)
    val outCount = MutableLiveData(0)

    var isTop = true
    var lineUp = mutableListOf(EventPlayer())


    private var inningCount = 1
    private var totalInning = 18

    // record previous pitcher's inning pitched (counting by per out)
    private var previousIp = 0

    // the current pitcher's pitching count
    private var pitcherCount = 1

    // total count including strike and foul ball
    private var totalStrike = 0

    init {
        lineUp = guestLineUp
        nextAtBat.value = lineUp.lineUpPlayer(1).name
    }

    var baseList = arrayOf<EventPlayer?>(lineUp[0], null, null, null)
    var atBaseList = mutableListOf<AtBase>()

    var atBatNumber = 0
    var atBatName = MutableLiveData<String>().apply {
        value = "第1棒 ${baseList[0]?.name}"
    }

    val firstBaseVisible = MutableLiveData<Boolean>(false)
    val firstBaseName = MutableLiveData<String>("")
    val secondBaseVisible = MutableLiveData<Boolean>(false)
    val secondBaseName = MutableLiveData<String>("")
    val thirdBaseVisible = MutableLiveData<Boolean>(false)
    val thirdBaseName = MutableLiveData<String>("")

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



    fun scored(score: Int) {
        if (isTop) {
            guestRun.value = guestRun.value!!.plus(score)
        } else {
            homeRun.value = homeRun.value!!.plus(score)
        }
        // update game box
        game.value?.let {
            it.box.score[inningCount - 1] += score
            it.box.run[(inningCount - 1) % 2] += score
        }

        uploadGameBox()
    }

    private fun uploadGameBox() {
        viewModelScope.launch {
            game.value?.let {
                repository.updateGameBox(it.id, it.box)
            }
        }
    }

    private fun switchCheck() {
        when (inningCount) {
            totalInning -> {
                game.value?.let {
                    // if tie (home score = guest score)
                    if (it.box.run[0] == it.box.run[1] && totalInning <= 22) {
                        totalInning += 2
                        switch()
                    } else {
                        switchFinal()
                    }
                }
            }
            // the final inning will be skipped if home scores more than guest team
            (totalInning - 1) -> {
                game.value?.let {
                    if (it.box.run[0] < it.box.run[1]) {
                        switchFinal()
                    } else {
                        switch()
                    }
                }
            }
            else -> switch()
        }
    }

    fun switch() {
        atBatNumber = nextAtBatNumber(atBatNumber, lineUp.size)
        clearCount(includeOut = true)
        inningCount += 1

        sendEvent(Event(
                inning = inningCount,
                result = EventType.INNINGCHANGE.number
        ))

        recordTeamStatus()

        nextAtBat.value = lineUp.lineUpPlayer(atBatNumber + 1).name

        uploadGameBox()
        game.value!!.box.score.add(0)

        isTop = !isTop
        liveIsTop.value = isTop
        baseList = arrayOf(lineUp[atBatNumber], null, null, null)
        atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]?.name}"
        updateRunnerUI()
    }

    private fun recordTeamStatus() {
        if (isTop) {
            // record current pitching count
            homePitchCount = liveBallCount.value!!
            // switch pitcher pitch count
            liveBallCount.value = guestPitchCount
            // record current last batting order
            guestABNumber = atBatNumber
            // start next inning batting order
            atBatNumber = homeABNumber
            // switch line up to other team
            lineUp = homeLineUp
            pitcherName.value = guestPitcher.name
            inningShow.value = "${(inningCount / 2)}"
        } else {
            guestPitchCount = liveBallCount.value!!
            liveBallCount.value = homePitchCount
            homeABNumber = atBatNumber
            atBatNumber = guestABNumber
            lineUp = guestLineUp
            pitcherName.value = homePitcher.name
            inningShow.value = "${(inningCount / 2) + 1}"
        }
    }

    private fun nextAtBatNumber(currentNum: Int, lineUpSize: Int): Int {
        // if current at bat is the last player of line up
        return if (currentNum == (lineUpSize - 1)) {
            0
        } else {
            (currentNum + 1)
        }
    }

    fun switchFinal() {
        val totalInningPitched = (((inningCount + 1) / 2 - 1) * 3 + outCount.value!!) - previousIp

        sendEvent(
                Event(
                        pitcher = if (isHome) homePitcher else guestPitcher,
                        inning = if (isHome) 1 else 2,
                        result = EventType.IPEND.number, //52
                        out = totalInningPitched
                )
        )

        uploadGameBox()
        _navigateToFinal.value = MyGame(isHome = isHome, game = game.value!!, benchPlayer = myBench)
    }

    fun ball() {
        addPitchCount()
        ballCount.value = ballCount.value!!.plus(1)
        if (ballCount.value == 4) {
            // single
            ballFour()
        }
    }

    private fun ballFour() {
        val toBeSend = Event(player = lineUp[atBatNumber],
                pitcher = if (isTop) homePitcher else guestPitcher,
                inning = inningCount,
                result = EventType.WALK.number,
                ball = 4,
                strike = totalStrike,
                out = outCount.value ?: 0)
        val hasRbi = advanceBase(0)
        toBeSend.currentBase = toCustomBaseInt(baseList = baseList)

        if (hasRbi) toBeSend.rbi = 1

        sendEvent(toBeSend)
        clearCount(includeOut = false)
        nextPlayer()
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

    fun out(totalOut: Int = 1) {
        outCount.value = outCount.value!!.plus(totalOut)
        if (outCount.value!! >= 3) {
            // three out! switch
            switchCheck()
        } else {
            nextPlayer()
        }
    }

    fun addPitchCount() {
        liveBallCount.value = liveBallCount.value!!.plus(1)
    }

    // two situation 1. pick off 2. steal base fail
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

        if (outCount.value!! == 3) switchCheck()

        updateRunnerUI()
    }

    fun toEventDialog(isSafe: Boolean) {
        atBaseList.clear()
        val hitterEvent = Event(
                inning = inningCount,
                ball = ballCount.value ?: 0,
                strike = totalStrike,
                out = outCount.value ?: 0,
                player = lineUp[atBatNumber],
                pitcher = if (isTop) homePitcher else guestPitcher
        )

        baseList[0] = lineUp[atBatNumber]
        // create at base list for event pager
        for ((index, player) in baseList.withIndex()) {
            if (player != null) {
                atBaseList.add(AtBase(base = index, player = player))
            }
        }

        // player list for error recording
        val tenOnFieldPlayer = mutableListOf<EventPlayer>()
        if (isTop == isHome) {
            if (isHome) {
                tenOnFieldPlayer.addAll(homeLineUp)
                tenOnFieldPlayer.add(homePitcher)
            } else {
                tenOnFieldPlayer.addAll(guestLineUp)
                tenOnFieldPlayer.add(guestPitcher)
            }
        }

        // if the hitter safe
        if (isSafe) {
            _navigateToEvent.value = EventInfo(gameId = argument.game.id,
                    atBaseList = atBaseList,
                    isSafe = true,
                    isDefence = (isTop == isHome),
                    hitterPreEvent = hitterEvent,
                    onField = tenOnFieldPlayer)
        } else {
            // only care about hitter if two out
            if (outCount.value == 2) {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = mutableListOf(AtBase(0, baseList[0]!!)),
                        isSafe = false,
                        isDefence = (isTop == isHome),
                        hitterPreEvent = hitterEvent,
                        onField = tenOnFieldPlayer,
                        baseForThreeOut = toCustomBaseInt(baseList = baseList))

            } else {
                _navigateToOut.value = EventInfo(gameId = argument.game.id,
                        atBaseList = atBaseList,
                        isSafe = false,
                        isDefence = (isTop == isHome),
                        hitterPreEvent = hitterEvent,
                        onField = tenOnFieldPlayer)
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

    // create integer argument for custom base view
    private fun toCustomBaseInt(baseList: Array<EventPlayer?>): Int {
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

    // player advance base until the base is empty
    fun advanceBase(myself: Int): Boolean {
        var hasRbi = false
        var end = myself
        while (baseList[end] != null) {
            if (end == 3) {
                // when runner return home (score)
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
        updateRunnerUI()

        return (hasRbi)
    }

    private fun sendEvent(event: Event) {
        viewModelScope.launch {
            repository.sendEvent(argument.game.id, event)
        }
    }

    fun nextPlayer() {
        clearCount(includeOut = false)
        atBatNumber = nextAtBatNumber(atBatNumber, lineUp.size)

        // change the hitter to next person in line
        baseList[0] = lineUp[atBatNumber]
        atBatName.value = "第${atBatNumber + 1}棒 ${baseList[0]!!.name}"

        nextAtBat.value = lineUp.lineUpPlayer(atBatNumber + 1).name
        updateRunnerUI()
    }

    private fun updateRunnerUI() {
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
    }

    fun pinch(player: EventPlayer, position: Int) {
        player.order = lineUp[atBatNumber].order + 1

        if (isTop) {
            guestLineUp[atBatNumber] = player
        } else {
            homeLineUp[atBatNumber] = player
        }

        atBatName.value = player.name
        baseList[0] = lineUp[atBatNumber]

        // remove player from bench
        myBench.removeAt(position)
    }

    fun nextPitcher(next: EventPlayer, position: Int) {
        // total inning pitched will be counted by total player out. for example, on full inning = 3 outs
        val totalInningPitched = (((inningCount + 1) / 2 - 1) * 3 + outCount.value!!) - previousIp
        previousIp += totalInningPitched

        sendEvent(
                Event(
                        pitcher = if (isTop) homePitcher else guestPitcher,
                        inning = inningCount,
                        result = EventType.INNINGSPITCHED.number,
                        out = totalInningPitched
                )
        )

        // total count of pitcher
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
        pitcherName.value = next.name
        myBench.removeAt(position)
    }

    // set up new base list after event done
    fun setNewBaseList(newList: Array<EventPlayer?>) {
        baseList = newList
    }

    private fun clearCount(includeOut: Boolean) {
        totalStrike = 0
        ballCount.value = 0
        strikeCount.value = 0
        if (includeOut) outCount.value = 0
    }

    fun clearBaseList() {
        baseList = arrayOf<EventPlayer?>(null, null, null, null)
    }

    fun navigateToPinch() {
        _navigateToPinch.value = true
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

}