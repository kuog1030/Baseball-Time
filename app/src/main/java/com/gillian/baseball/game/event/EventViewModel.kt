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

    val atBaseList = eventInfo.atBaseList

    var hitterEvent = MutableLiveData<Event>(eventInfo.hitterPreEvent)
    var newBaseList = arrayOf<EventPlayer?>(null, null, null, null)
    var hasBaseOut = mutableListOf<Int>()


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
        atBaseList[0].event = eventInfo.hitterPreEvent
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

    fun getDetailString() : String {
        var result = ""
        var count = 0
        for (atBase in atBaseList) {
            atBase.eventType?.let{
                count += 1
                result = result.plus("$count. ${atBase.player.name}${it.broadcast}\n")
            }
        }
        return result
    }


    // rbi是由跑者回壘得分run()驅使的
    fun saveAndDismiss() {

        // 好壞球會在真正要送出前才記錄
        atBaseList[0].event?.let{
            if (it.result == EventType.HITBYPITCH.number){
                it.ball += 1
            } else {
                it.strike += 1
            }
            // 壘上跑者的出局數送出前加上去
            if (hasBaseOut.isNotEmpty()) {
                it.out += hasBaseOut.size
            }
            it.currentBase = customBaseInt.value ?: 0
            scoreToBeAdded = it.rbi
            hitToBeAdded = when (it.result) {
                EventType.SINGLE.number -> 1
                EventType.DOUBLE.number -> 1
                EventType.TRIPLE.number -> 1
                EventType.HOMERUN.number -> 1
                else -> 0
            }
        }

        Log.i("remote", "--------------from event viewmodel save and dismiss--------------")
        viewModelScope.launch {
            for (player in atBaseList) {
                player.event?.let { eventToSend ->
                    repository.sendEvent(eventInfo.gameId, eventToSend)
                }
            }
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
        atBaseList[0].eventType = when (baseCount) {
            1 -> EventType.SINGLE
            2 -> EventType.DOUBLE
            else -> EventType.TRIPLE
        }
        atBaseList[0].event?.let{
            it.result = baseCount
        }
        atBaseList[0].base = baseCount
        changeToNextPage()
    }

    fun homerun() {
        atBaseList[0].eventType = EventType.HOMERUN
        atBaseList[0].event?.let{
            it.result = EventType.HOMERUN.number
            it.run = 1
            it.rbi = 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun hbp() {
        atBaseList[0].eventType = EventType.HITBYPITCH
        atBaseList[0].event?.let{
            it.result = EventType.HITBYPITCH.number
            it.strike -= 1 // 之後event統一會加strike，所以先扣掉XD
            it.ball += 1
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // 應該要改成勾勾? 失誤上一壘 失誤上二壘 失誤上三壘
    // 一安+失誤?????好可怕
    //TODO()
    fun error() {
        atBaseList[0].eventType = EventType.ERRORONBASE
        atBaseList[0].event?.let{
            it.result = EventType.ERRORONBASE.number
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }

    fun droppedThird() {
        atBaseList[0].eventType = EventType.DROPPEDTHIRD
        atBaseList[0].event?.let{
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

    fun thirdBase(atBase: AtBase) {
        atBase.event = null
        atBase.base = 3
        changeToNextPage()
    }

    // 回壘得分
    fun run(atBase: AtBase) {
        atBase.base = -1
        atBase.eventType = EventType.RUN
        atBase.event = Event(inning = hitterEvent.value!!.inning,
            result = EventType.RUN.number,
            run = 1,
            player = atBase.player,
            pitcher = hitterEvent.value?.pitcher!!,
            out = hitterEvent.value?.out ?: 0)

        atBaseList[0].event?.let{
            it.rbi += 1
        }
        changeToNextPage()
    }

    // 打者按了野手選擇"上壘"
    fun fielderChoice() {
        // default single
        atBaseList[0].eventType = EventType.FIELDERCHOICE
        atBaseList[0].event?.let{
            it.result = EventType.FIELDERCHOICE.number
        }
        atBaseList[0].base = 1
        changeToNextPage()
    }


    // 例如野手選擇的跑者出局
    fun runnerOut(atBase: AtBase) {
        atBase.event = null
        hasBaseOut.add(atBase.base)
        atBase.base = -1
        changeToNextPage()
    }

    /* ------------- 以下是按了出局會有的三個選項 --------------- */

    fun groundOut() {
        atBaseList[0].eventType = EventType.GROUNDOUT
        atBaseList[0].event?.let{
            it.result = EventType.GROUNDOUT.number
            it.out += 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    // 高飛犧牲打是air out (true)
    fun airOut(hasRbi: Boolean) {
        atBaseList[0].eventType = if (hasRbi) EventType.SACRIFICEFLY else EventType.AIROUT
        atBaseList[0].event?.let{
            it.result = if (hasRbi) EventType.SACRIFICEFLY.number else EventType.AIROUT.number
            it.out += 1
        }
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun initNextEvent() {
        newBaseList = arrayOf(null, null, null, null)
    }

    fun changeToNextPage() {
        baseListToCustom()
        eventDetail.value = getDetailString()
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