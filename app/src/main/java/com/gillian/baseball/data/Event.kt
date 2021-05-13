package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var id: String = "",
    var inning: Int = 0,
    var result: Int = 0,
    var ball: Int = 0,
    var strike: Int = 0,
    var out: Int = 0,
    var rbi: Int = 0,
    var run: Int = 0,
    val player: EventPlayer = EventPlayer()
//    val hitter: EventPlayer? = null,
//    val pitcher: EventPlayer? = null,
//    // runner will only be recorded when
//    // 1. on base event eg. steal base, advance by error
//    val runner: EventPlayer? = null
) : Parcelable