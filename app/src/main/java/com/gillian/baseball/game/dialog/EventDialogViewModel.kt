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
import kotlinx.coroutines.launch

//class HitterViewModel(private val repository: BaseballRepository) : ViewModel() {
class EventDialogViewModel(private val repository: BaseballRepository) : ViewModel() {

    var atBaseList = listOf<AtBase>()
    var hitterEvent = MutableLiveData<Event>()
    var newBaseList = arrayOf<EventPlayer?>(null, null, null, null)


    var eventList = mutableListOf<Event>()

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
    }

    fun saveAndDismiss() {
        val readyToSend = eventList
        viewModelScope.launch {
            for (singleEvent in readyToSend) {
                repository.sendEvent(singleEvent)
            }
        }
        initNextEvent()

        for (atBase in atBaseList) {
            if (atBase.base != -1) {
                newBaseList[atBase.base] = atBase.player
            }
        }
        dismissDialog()
    }

    fun single() {
        hitterEvent.value?.let{
            it.result = 1
        }
        eventList.add(hitterEvent.value!!)
        // atBaseList[0] is hitter
        atBaseList[0].base = 1


        changeToNextPage()
    }

    fun Double() {
        hitterEvent.value?.let{
            it.result = 2
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = 2

        changeToNextPage()
    }

    fun triple() {
        hitterEvent.value?.let{
            it.result = 3
        }

        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = 3
        changeToNextPage()
    }

    fun homerun() {
        hitterEvent.value?.let{
            it.result = 4
        }
        eventList.add(hitterEvent.value!!)
        atBaseList[0].base = 4
        changeToNextPage()
    }

    fun hbp() {
        hitterEvent.value?.let{
            it.result = 5
        }
        atBaseList[0].base = 1
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

    fun secondBase(atBase: AtBase) {
        atBase.base = 2
        changeToNextPage()
    }

    fun thirdBase(atBase: AtBase) {
        atBase.base = 3
        changeToNextPage()
    }

    fun run(atBase: AtBase) {
        atBase.base = -1
        eventList.add(Event(run = 1, player = atBase.player))
        changeToNextPage()
    }

    fun initNextEvent() {
        // debugging
        for (event in eventList) {
            Log.i("at base event", "send event $event")
        }
        eventList.clear()
        newBaseList = arrayOf(null, null, null, null)
    }
}