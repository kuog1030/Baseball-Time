package com.gillian.baseball.game.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.data.EventInfo
import com.gillian.baseball.game.EventType
import kotlinx.coroutines.launch

class EventViewModel(private val repository: BaseballRepository, private val eventInfo: EventInfo) : ViewModel() {

    val atBaseList = eventInfo.atBaseList

    val hitter = eventInfo.hitterPreEvent

    // defending players (on field)
    val onFieldPlayer = eventInfo.onField

    // to record the base condition after event ends
    private lateinit var newBaseList : Array<EventPlayer?>

    var hasBaseOut = mutableListOf<Int>()

    // the detail of all events for confirm page
    val eventDetail = MutableLiveData<String>("")

    var scoreToBeAdded = 0
    var hitToBeAdded = 0

    // in order to record error for my player stat
    var errorEvent: Event? = null

    // only record error if user is fielding (defence)
    val showErrorList = MutableLiveData<Boolean>(false)

    private var _changeToNextPage = MutableLiveData<Boolean>()

    val changeToNextPage: LiveData<Boolean>
        get() = _changeToNextPage

    private var _dismiss = MutableLiveData<Array<EventPlayer?>>()

    val dismiss: LiveData<Array<EventPlayer?>>
        get() = _dismiss


    val customBaseInt = MutableLiveData(0)


    init {
        atBaseList[0].event = eventInfo.hitterPreEvent
    }


    private fun baseListToCustom() {
        var customInt = 0
        for (atBase in atBaseList) {
            when (atBase.base) {
                1 -> customInt += 1
                2 -> customInt += 10
                3 -> customInt += 100
            }
        }
        customBaseInt.value = customInt
    }

    private fun getDetailString(): String {
        var result = ""
        var count = 0
        for (atBase in atBaseList) {
            atBase.eventType?.let {
                count += 1
                result = result.plus("$count. ${atBase.player.name}${it.broadcast}\n")
            }
        }
        errorEvent?.let {
            result = result.plus("${it.player.name}發生失誤")
        }

        return result
    }


    // hitter's rbi will only be triggered by runner (run())
    fun saveAndDismiss() {

        // complete hitter's event right before event sending
        atBaseList[0].event?.let {
            // modify ball count
            if (it.result == EventType.HITBYPITCH.number) {
                it.ball += 1
            } else {
                it.strike += 1
            }

            // add total out count
            if (hasBaseOut.isNotEmpty()) {
                it.out += hasBaseOut.size
            }
            it.currentBase = eventInfo.baseForThreeOut ?: customBaseInt.value ?: 0
            hitToBeAdded = when (it.result) {
                EventType.SINGLE.number -> 1
                EventType.DOUBLE.number -> 1
                EventType.TRIPLE.number -> 1
                EventType.HOMERUN.number -> 1
                else -> 0
            }
            // score to be added will base on rbi first. Rbi might be modified afterwards
            scoreToBeAdded = it.rbi
            // hitter get no rbi if double play
            if (hasBaseOut.size == 2) {
                it.rbi = 0
            }
        }

        viewModelScope.launch {
            for (player in atBaseList) {
                player.event?.let { eventToSend ->
                    repository.sendEvent(eventInfo.gameId, eventToSend)
                }
            }
            errorEvent?.let {
                repository.sendEvent(eventInfo.gameId, it)
            }
        }

        newBaseList = createNewBaseList(atBaseList)

        dismissDialog()
    }

    private fun createNewBaseList(atBaseList: List<AtBase>) : Array<EventPlayer?> {
        val result = arrayOf<EventPlayer?>(null, null, null, null)
        for (atBase in atBaseList) {
            if (atBase.base != -1) {
                result[atBase.base] = atBase.player
            }
        }
        return result
    }

    fun hit(baseCount: Int) {
        errorEvent = null
        atBaseList[0].eventType = when (baseCount) {
            1 -> EventType.SINGLE
            2 -> EventType.DOUBLE
            else -> EventType.TRIPLE
        }
        atBaseList[0].event?.let {
            it.result = baseCount
        }
        atBaseList[0].base = baseCount
        changeToNextPage()
    }

    fun homerun() {
        errorEvent = null
        atBaseList[0].eventType = EventType.HOMERUN
        atBaseList[0].event?.let {
            it.result = EventType.HOMERUN.number
            it.run = 1
            it.rbi = 1
        }
        atBaseList[0].base = -1

        for ((index, runner) in atBaseList.withIndex()) {
            if (index == 0) continue
            run(runner, false)
        }

        changeToNextPage(true)
    }

    fun hbp() {
        errorEvent = null
        atBaseList[0].eventType = EventType.HITBYPITCH
        atBaseList[0].event?.let {
            it.result = EventType.HITBYPITCH.number
            it.strike -= 1 // 之後event統一會加strike，所以先扣掉XD
            it.ball += 1
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }


    fun error() {
        atBaseList[0].eventType = EventType.ERRORONBASE
        atBaseList[0].event?.let {
            it.result = EventType.ERRORONBASE.number
        }
        atBaseList[0].base = 1

        // only record which player error when defencing
        if (eventInfo.isDefence) {
            showErrorList.value = true
        } else {
            changeToNextPage()
        }
    }

    fun recordError(player: EventPlayer) {
        errorEvent = Event(inning = hitter.inning,
                result = EventType.ERROR.number,
                player = player)
        changeToNextPage()
    }

    fun droppedThird() {
        errorEvent = null
        atBaseList[0].eventType = EventType.DROPPEDTHIRD
        atBaseList[0].event?.let {
            it.result = EventType.DROPPEDTHIRD.number
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }


    fun toBase(atBase: AtBase, base: Int) {
        atBase.event = null
        atBase.base = base
        changeToNextPage()
    }

    // runner return home base and score down
    fun run(atBase: AtBase, changePage: Boolean = true) {
        atBase.base = -1
        atBase.eventType = EventType.RUN
        atBase.event = Event(inning = hitter.inning,
                result = EventType.RUN.number,
                run = 1,
                player = atBase.player,
                pitcher = hitter.pitcher,
                out = hitter.out)

        atBaseList[0].event?.let {
            it.rbi += 1
        }
        if (changePage) changeToNextPage()
    }

    // event safe -> hitter fielder choice (no hit)
    fun fielderChoice() {
        errorEvent = null
        atBaseList[0].eventType = EventType.FIELDERCHOICE
        atBaseList[0].event?.let {
            it.result = EventType.FIELDERCHOICE.number
        }
        // hitter default reach first base
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // out event such as fielder choice
    fun runnerOut(atBase: AtBase) {
        atBase.event = null
        atBase.eventType = EventType.ONBASEOUT
        hasBaseOut.add(atBase.base)
        atBase.base = -1
        changeToNextPage()
    }

    /* ------------- Below the options for "out" --------------- */

    fun groundOut() {
        atBaseList[0].eventType = EventType.GROUNDOUT
        atBaseList[0].event?.let {
            it.result = EventType.GROUNDOUT.number
            it.out += 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    // sacrifice fly would be air out (true)
    fun airOut(isSacrifice: Boolean) {
        atBaseList[0].eventType = if (isSacrifice) EventType.SACRIFICEFLY else EventType.AIROUT
        atBaseList[0].event?.let {
            it.result = if (isSacrifice) EventType.SACRIFICEFLY.number else EventType.AIROUT.number
            it.out += 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun sacrificeHit() {
        atBaseList[0].eventType = EventType.SACRIFICEHIT
        atBaseList[0].event?.let {
            it.result = EventType.SACRIFICEHIT.number
            it.out += 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun changeToNextPage(toEnd: Boolean = false) {
        showErrorList.value = false
        baseListToCustom()
        eventDetail.value = getDetailString()
        _changeToNextPage.value = toEnd
    }

    fun onNextPageChanged() {
        _changeToNextPage.value = null
    }

    fun dismissDialog() {
        _dismiss.value = newBaseList
    }

    fun onDialogDismiss() {
        _dismiss.value = null
        hasBaseOut.clear()
    }
}