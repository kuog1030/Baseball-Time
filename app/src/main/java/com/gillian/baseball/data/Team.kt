package com.gillian.baseball.data

data class Team (
    var id : String = "",
    var name : String = "",
    var acronym : String = name,
    var color : String = "",
    var member : MutableList<Player> = mutableListOf(),
    var membersId : MutableList<String> = mutableListOf()
)

data class Player(
    var id : String = "",
    var userId : String? = null,
    var teamId : String? = null,
    var name : String = "",
    var nickname : String? = null,
    var number : String = "",
    var image: String? = null
)