package com.gillian.baseball.data

data class GameCard(
        var id: String = "",
        val title: String = "",
        var date: Long = -1,
        var isHome: Boolean = false,
        var homeScore : Int = -1,
        var guestScore : Int = -1,
        val myScore : List<String> = listOf()
        )

//@Parcelize
//data class Game (
//    var id: String = "",
//    var name: String = "",
//    var date: Long = -1,
//    var place: String = "",
//    var home: GameTeam = GameTeam(),
//    var guest: GameTeam = GameTeam(),
//    val box: Box = Box()
//) : Parcelable