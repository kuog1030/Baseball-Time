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

    var eventList = mutableListOf<Event>()
    var event = MutableLiveData<Event>()

    private var _changeToNextPage = MutableLiveData<Boolean>()
    val changeToNextPage: LiveData<Boolean>
        get() = _changeToNextPage

    private var _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean>
        get() = _dismiss

    fun changeToNextPage() {
        _changeToNextPage.value = true
    }

    fun onNextPageChanged() {
        _changeToNextPage.value = null
    }

    fun dismissDialog() {
        _dismiss.value = true
    }

    fun onDialogDismiss() {
        _dismiss.value = null
    }

    fun saveAndDismiss() {
        Log.i("gillian", "event list count ${eventList.size}, the event now is ${event.value}")
        val readyToSend = eventList
        viewModelScope.launch {
            for (singleEvent in readyToSend) {
                repository.sendEvent(singleEvent)
            }
        }
        eventList.clear()
        dismissDialog()
    }

    fun single() {
        event.value?.let{
            it.result = 1
        }
        eventList.add(event.value!!)
        changeToNextPage()
    }

    fun Double() {
        event.value?.let{
            it.result = 2
        }
        changeToNextPage()
    }

    fun triple() {
        event.value?.let{
            it.result = 3
        }
        changeToNextPage()
    }

    fun homerun() {
        event.value?.let{
            it.result = 4
        }
        changeToNextPage()
    }

    fun hbp() {
        event.value?.let{
            it.result = 5
        }
    }

    fun error() {
        event.value?.let{
            it.result = 6
        }
    }

    fun droppedThird() {
        event.value?.let{
            it.result = 7
        }
    }

    fun secondBase() {
        changeToNextPage()
    }

    fun thirdBase() {
        changeToNextPage()
    }

    fun run() {
        eventList.add(Event())
        changeToNextPage()
    }
}