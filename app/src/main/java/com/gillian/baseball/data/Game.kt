package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Game (
    var id: String = "",
    var name: String = "",
    var date: Long = -1,
    var place: String = "",
    var home: GameTeam = GameTeam(),
    var guest: GameTeam = GameTeam(),
    val box: Box = Box()
) : Parcelable

@Parcelize
data class Box (
    val score: MutableList<Int> = mutableListOf(0),
    val run: MutableList<Int> = mutableListOf(0, 0),
    val hit: MutableList<Int> = mutableListOf(0, 0),
    val error: MutableList<Int> = mutableListOf(0, 0)
) : Parcelable

@Parcelize
data class GameTeam (
    val name: String = "",
    val teamId: String = "",
    var pitcher: EventPlayer = EventPlayer(),
    val lineUp: MutableList<EventPlayer> = mutableListOf()
) : Parcelable

@Parcelize
data class MyGame (
        val isHome : Boolean? = null,
        val game : Game
) : Parcelable