package com.gillian.baseball.game.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.source.BaseballRepository

//class HitterViewModel(private val repository: BaseballRepository) : ViewModel() {
class EventDialogViewModel : ViewModel() {

    var temp = MutableLiveData<String>("kuo")
    var tempList = mutableListOf<String>()

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
        dismissDialog()
    }

    fun single() {
        tempList.add("single")
        changeToNextPage()
    }

    fun Double() {
        tempList.add("double")
        changeToNextPage()
    }

    fun triple() {
        tempList.add("triple")
        changeToNextPage()
    }

    fun homerun() {
        tempList.add("homerun")
        changeToNextPage()
    }

    fun hbp() {
        for (i in tempList) {
            Log.i("gillian", "temp list contains $i")
        }
    }

    fun error() {
        Log.i("gillian", "temp list total is ${tempList.size}")
    }

    fun droppedThird(){}

}