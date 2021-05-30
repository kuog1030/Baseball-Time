package com.gillian.baseball.broadcast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.source.BaseballRepository
import kotlinx.coroutines.launch

class BroadcastViewModel(private val repository: BaseballRepository) : ViewModel() {


    var liveEvents = MutableLiveData<List<Event>>()

    init {
        getLiveEvent()
    }

    fun getLiveEvent() {
        viewModelScope.launch {
            liveEvents = repository.getLiveEvents("J6SPWwBKBZgjjsCGHUUa")
        }

    }


}