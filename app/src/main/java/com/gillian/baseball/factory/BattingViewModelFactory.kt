package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.source.BaseballRepository

//
//class BattingViewModelFactory constructor(
//        private val repository: BaseballRepository,
//        private var lineup: List<EventPlayer>
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>) =
//        with(modelClass) {
//            when {
//                isAssignableFrom(BattingViewModel::class.java) ->
//                    BattingViewModel(repository, lineup)
//                else ->
//                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        } as T
//}