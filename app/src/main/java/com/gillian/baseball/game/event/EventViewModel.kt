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

    var atBaseList = eventInfo.atBaseList
    var hitterEvent = MutableLiveData<Event>(eventInfo.hitterPreEvent)
    var newBaseList = arrayOf<EventPlayer?>(null, null, null, null)
    var hasOut: Boolean? = null
    var hasBaseOut = mutableListOf<Int>()


    var eventList = mutableListOf<Event>()
    var eventDetail = MutableLiveData<String>("")

    var scoreToBeAdded = 0
    var hitToBeAdded = 0

    private var _changeToNextPage = MutableLiveData<Boolean>()
    val changeToNextPage: LiveData<Boolean>
        get() = _changeToNextPage

    private var _dismiss = MutableLiveData<Array<EventPlayer?>>()
    val dismiss: LiveData<Array<EventPlayer?>>
        get() = _dismiss

    fun changeToNextPage() {
        _changeToNextPage.value = true
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
        hasOut = null
    }

    fun saveAndDismiss() {
        val readyToSend = eventList
        // 好壞球會在真正要送出前才記錄
        if (eventList[0].result == EventType.HITBYPITCH.number) {
            readyToSend[0].ball += 1
        } else {
            readyToSend[0].strike += 1
        }
        scoreToBeAdded = eventList[0].rbi
        viewModelScope.launch {
            for (singleEvent in readyToSend) {
                repository.sendEvent(eventInfo.gameId, singleEvent)
            }
        }

        // 安打數加上去box
        hitToBeAdded = when (eventList[0].result) {
            EventType.SINGLE.number -> 1
            EventType.DOUBLE.number -> 1
            EventType.TRIPLE.number -> 1
            EventType.HOMERUN.number -> 1
            else -> 0
        }

        initNextEvent()

        for (atBase in atBaseList) {
            if (atBase.base != -1) {
                newBaseList[atBase.base] = atBase.player
                //Log.i("at base", "when save and dismiss ${atBase.base} ${atBase.player}")
            }
        }
        dismissDialog()
    }

    fun hit(baseCount: Int) {
        hitterEvent.value?.let {
            it.result = baseCount
        }
        eventList.add(hitterEvent.value!!)
        // atBaseList[0] is hitter
        atBaseList[0].base = baseCount

        changeToNextPage()
    }

    fun homerun() {
        hitterEvent.value?.let {
            it.result = EventType.HOMERUN.number
            it.run = 1
            it.rbi = 1
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun hbp() {
        hitterEvent.value?.let {
            it.result = EventType.HITBYPITCH.number
            // 之後event統一會加strike，所以先扣掉XD
            it.strike -= 1
            it.ball += 1
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // 應該要改成勾勾? 失誤上一壘 失誤上二壘 失誤上三壘
    // 一安+失誤?????好可怕
    //TODO()
    fun error() {
        hitterEvent.value?.let {
            it.result = EventType.ERRORONBASE.number
        }
    }

    fun droppedThird() {
        hitterEvent.value?.let {
            it.result = EventType.DROPPEDTHIRD.number
        }
        atBaseList[0].base = 1
    }

    fun fielderChoice() {
        // default single
        hitterEvent.value?.let {
            it.result = EventType.FIELDERCHOICE.number
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = 1
        changeToNextPage()
    }

    fun toBase(atBase: AtBase, base: Int) {
        atBase.base = base
        changeToNextPage()
    }

    fun thirdBase(atBase: AtBase) {
        atBase.base = 3
        changeToNextPage()
    }

    // 回壘得分
    fun run(atBase: AtBase) {
        atBase.base = -1
        eventList.add(Event(inning = hitterEvent.value!!.inning,
                result = EventType.RUN.number,
                run = 1,
                player = atBase.player,
                pitcher = hitterEvent.value?.pitcher!!,
                out = hitterEvent.value?.out ?: 0))
        eventList[0].rbi += 1

        eventDetail.value = eventDetail.value.plus("${atBase.player.number}號 ${atBase.player.name} 回壘得分 + 1\n")
        changeToNextPage()
    }

    fun fielderChoiceOut(atBase: AtBase) {
        hasBaseOut.add(atBase.base)
        atBase.base = -1
        changeToNextPage()
    }

    fun groundOut() {
        hitterEvent.value?.let {
            it.result = EventType.GROUNDOUT.number
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = -1
        hasOut = true
        changeToNextPage()
    }


    fun airOut(hasRbi: Boolean) {
        hitterEvent.value?.let {
            it.result = if (hasRbi) EventType.SACRIFICEFLY.number else EventType.AIROUT.number
            //it.rbi = if (hasRbi) 1 else 0  跑者回壘得分的時候就會加進打者的event
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = -1
        hasOut = true
        changeToNextPage()
    }

    fun initNextEvent() {
        eventList.clear()
        newBaseList = arrayOf(null, null, null, null)
    }
}