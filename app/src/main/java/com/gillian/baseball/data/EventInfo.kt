package com.gillian.baseball.data

import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event

data class EventInfo (
    val gameId: String,
    val atBaseList: List<AtBase>,
    val isSafe: Boolean,
    val hitterPreEvent: Event
)

//@Parcelize
//data class OnBaseInfo(
//        val gameId: String,
//        val inning: Int,
//        val out: Int,
//        val onClickPlayer: Int,
//        val pitcher: EventPlayer?,
//        val baseList: List<EventPlayer?>
//) : Parcelable