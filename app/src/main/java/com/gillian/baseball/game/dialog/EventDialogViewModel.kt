package com.gillian.baseball.game.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.game.EventType
import kotlinx.coroutines.launch

class EventDialogViewModel(private val repository: BaseballRepository) : ViewModel() {

    var atBaseList = listOf<AtBase>()
    var hitterEvent = MutableLiveData<Event>()
    var newBaseList = arrayOf<EventPlayer?>(null, null, null, null)
    var hasOut : Boolean? = null
    var hasBaseOut : Int? = null


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
        hasBaseOut = null
        hasOut = null
    }

    fun saveAndDismiss() {
        val readyToSend = eventList
        // 好球會在真正要送出前才記錄
        readyToSend[0].strike += 1
        scoreToBeAdded = eventList[0].rbi
        viewModelScope.launch {
            for (singleEvent in readyToSend) {
                repository.sendEvent(singleEvent)
            }
        }

        // 安打數加上去box
        hitToBeAdded = when ( eventList[0].result ) {
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
        hitterEvent.value?.let{
            it.result = baseCount
        }
        eventList.add(hitterEvent.value!!)
        // atBaseList[0] is hitter
        atBaseList[0].base = baseCount

        changeToNextPage()
    }

    fun homerun() {
        hitterEvent.value?.let{
            it.result = EventType.HOMERUN.number
            it.run = 1
            it.rbi = 1
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = -1
        changeToNextPage()
    }

    fun hbp() {
        hitterEvent.value?.let{
            it.result = 5
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
        hitterEvent.value?.let{
            it.result = 6
        }
    }

    fun droppedThird() {
        hitterEvent.value?.let{
            it.result = 7
        }
        atBaseList[0].base = 1
    }

    fun fielderChoice(){
        // default single
        hitterEvent.value?.let{
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

    fun secondBase(atBase: AtBase) {
        atBase.base = 2
        changeToNextPage()
    }

    fun thirdBase(atBase: AtBase) {
        atBase.base = 3
        changeToNextPage()
    }

    // 回壘得分
    fun run(atBase: AtBase) {
        atBase.base = -1
        eventList.add(Event(run = 1,
                player = atBase.player,
                pitcher = hitterEvent.value?.pitcher!!,
                out = hitterEvent.value?.out ?: 0))
        eventList[0].rbi += 1

        eventDetail.value = eventDetail.value.plus("${atBase.player.number}號 ${atBase.player.name} 回壘得分 + 1\n")
        changeToNextPage()
    }

    fun fielderChoiceOut(atBase: AtBase) {
        hasBaseOut = atBase.base
        atBase.base = -1
        changeToNextPage()
    }

    fun groundOut() {
        hitterEvent.value?.let{
            it.result = 21
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = -1
        hasOut = true
        changeToNextPage()
    }


    fun airOut(hasRbi: Boolean) {
        hitterEvent.value?.let{
            it.result = if (hasRbi) 23 else 22
            it.rbi = if (hasRbi) 1 else 0
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