package com.gillian.baseball.data

data class GameCard(
        var id: String = "",
        val title: String = "",
        val place: String = "",
        var date: Long = -1,
        var status: Int = 0,
        var isHome: Boolean = false,
        var homeName: String = "",
        var guestName: String = "",
        var homeScore: Int = -1,
        var guestScore: Int = -1,
        val myScore: List<String> = listOf()
) {
    val dateString: String = ""
}

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