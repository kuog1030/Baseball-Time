package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyGame (
        val isHome : Boolean,
        val benchPlayer: MutableList<EventPlayer>,
        val game : Game
) : Parcelable

//
//val debugGame = MyGame(
//    isHome = false,
//        benchPlayer = mutableListOf((EventPlayer( playerId = "A", order = 100, name = "陳鏞基", number = "13", pinch = null)), EventPlayer( playerId = "B", order = 100, name = "蘇智傑", number = "32", pinch = null), EventPlayer( playerId = "C", order = 100, name = "陳傑憲", number = "24", pinch = null),
//                EventPlayer( playerId = "D", order = 100, name = "王柏融", number = "9", pinch = null),
//                EventPlayer( playerId = "E", order = 100, name = "林威助", number = "27", pinch = null)),
//        game = Game("123155", "第四周測試大賽", 1622708071588, "測試橋", )
//
//)
//
//GameTeam()