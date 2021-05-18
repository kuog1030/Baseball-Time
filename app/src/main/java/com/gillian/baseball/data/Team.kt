package com.gillian.baseball.data

data class Team (
    var id : String = "",
    var name : String = "",
    var color : String = "",
    var member : MutableList<Player>
)

data class Player(
    var id : String = "",
    var userId : String? = null,
    var teamId : String? = null,
    var name : String = "",
    var nickname : String? = null,
    var number : String = ""

)

//data class EventPlayer(
//    var userId: String = "",
//    val name: String = "",
//    val number: String = "",
//    var pinch: EventPlayer? = null
//) : Parcelable