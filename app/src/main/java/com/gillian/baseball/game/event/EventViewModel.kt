package com.gillian.baseball.game.event

import android.util.Log
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



    val customBaseInt = MutableLiveData<Int>(0)


    init {
        eventList.add(eventInfo.hitterPreEvent)
        Log.i("gillian", "event view model : init event list $eventList")
    }


    fun baseListToCustom() {
        var customInt = 0
        for (atBase in atBaseList) {
            Log.i("gillian", "at base is $atBase")
            when (atBase.base) {
                1 -> customInt += 1
                2 -> customInt += 10
                3 -> customInt += 100
            }
        }
        customBaseInt.value = customInt
    }


    // rbi是由跑者回壘得分run()驅使的
    fun saveAndDismiss() {
        val readyToSend = eventList
        // 好壞球會在真正要送出前才記錄
        if (eventList[0].result == EventType.HITBYPITCH.number) {
            readyToSend[0].ball += 1
        } else {
            readyToSend[0].strike += 1
        }

        // 壘上跑者的出局數送出前加上去
        if (hasBaseOut.isNotEmpty()) {
            readyToSend[0].out += hasBaseOut.size
        }

        readyToSend[0].currentBase = customBaseInt.value ?: 0
        scoreToBeAdded = eventList[0].rbi

        Log.i("remote", "--------------from event viewmodel save and dismiss--------------")
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
        eventList[0].result = baseCount
        // atBaseList[0] is hitter
        atBaseList[0].base = baseCount

        changeToNextPage()
    }

    fun homerun() {
        eventList[0].let{
            it.result = EventType.HOMERUN.number
            it.run = 1
            it.rbi = 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun hbp() {
        eventList[0].let{
            it.result = EventType.HITBYPITCH.number
            // 之後event統一會加strike，所以先扣掉XD
            it.strike -= 1
            it.ball += 1
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // 應該要改成勾勾? 失誤上一壘 失誤上二壘 失誤上三壘
    // 一安+失誤?????好可怕
    //TODO()
    fun error() {
        eventList[0].result = EventType.ERRORONBASE.number
        atBaseList[0].base = 1
        changeToNextPage()
    }

    fun droppedThird() {
        eventList[0].result = EventType.DROPPEDTHIRD.number
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

    // 打者按了野手選擇"上壘"
    fun fielderChoice() {
        // default single
        eventList[0].result = EventType.FIELDERCHOICE.number
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // 例如野手選擇的跑者出局
    fun runnerOut(atBase: AtBase) {
        hasBaseOut.add(atBase.base)
        atBase.base = -1
        changeToNextPage()
    }

    /* ------------- 以下是按了出局會有的三個選項 --------------- */

    fun groundOut() {
        eventList[0].result = EventType.GROUNDOUT.number
        eventList[0].out += 1
        atBaseList[0].base = -1
        changeToNextPage()
    }

    // 高飛犧牲打是air out (true)
    fun airOut(hasRbi: Boolean) {
        eventList[0].result = if (hasRbi) EventType.SACRIFICEFLY.number else EventType.AIROUT.number
        eventList[0].out += 1
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun initNextEvent() {
        eventList.clear()
        newBaseList = arrayOf(null, null, null, null)
    }

    fun changeToNextPage() {
        baseListToCustom()
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
    }
}