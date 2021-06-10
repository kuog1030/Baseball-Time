package com.gillian.baseball.data

import android.os.Parcelable
import com.gillian.baseball.game.EventType
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
    var currentBase: Int = 0,
    val player: EventPlayer = EventPlayer(),
    val pitcher: EventPlayer = EventPlayer(),
    var time: Long = -1
) : Parcelable {

    fun toLiveString() : String {
        var liveString = "第${player.order/100}棒 ${player.name}"

        if (strike > 7) {
            liveString += "在纏鬥了${strike+ball}球之下，"
        }

        for (type in EventType.values()) {
            if (result == type.number) {
                liveString += type.broadcast
            }
        }

        if (this.rbi != 0) {
            liveString += "帶有${this.rbi}分打點。"
        }

        if (this.out == 3) {
            liveString += "三人出局，換邊。"
        }

        return liveString
    }

    fun toOnBaseString() : String {
        var liveString = ""

        if (this.result == EventType.INNINGSPITCHED.number) {
            if (inning % 2 == 1) {
                liveString += "主隊換投。投手${pitcher.name}在投了${this.out}局後下場。"
            } else {
                liveString += "客隊換投。投手${pitcher.name}在投了${this.out}局後下場。"
            }
        } else {
            liveString += "壘上跑者${player.name}"
            for (type in EventType.values()) {
                if (this.result == type.number) {
                    liveString += type.broadcast
                }
            }
            if (this.out == 3) {
                liveString += "三人出局，換邊。"
            } else {
                liveString += "目前${this.out}出局。 "
            }
        }

        return liveString
    }
}