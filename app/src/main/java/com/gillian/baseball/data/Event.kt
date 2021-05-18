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
    val player: EventPlayer = EventPlayer(),
    val pitcher: EventPlayer = EventPlayer()
) : Parcelable {

//    override fun toString(): String {
//        return ("打者 ${player.name} 在 $strike 好球之下擊出了安打")
//    }
}