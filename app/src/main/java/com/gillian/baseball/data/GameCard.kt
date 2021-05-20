package com.gillian.baseball.data

data class GameCard(
        var id: String = "",
        val name: String = "",
        var date: Long = -1,



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