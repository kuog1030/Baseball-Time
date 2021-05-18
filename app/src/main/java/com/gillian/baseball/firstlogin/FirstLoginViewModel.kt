package com.gillian.baseball.firstlogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstLoginViewModel : ViewModel () {

    val teamName = MutableLiveData("")
    val playerName = MutableLiveData("")
    val playerNumber = MutableLiveData("")
}