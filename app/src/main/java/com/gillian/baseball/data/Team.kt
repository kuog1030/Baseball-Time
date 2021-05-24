package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team (
    var id : String = "",
    var name : String = "",
    var acronym : String = name,
    var color : String = "",
    var image : String = "",
    var member : MutableList<Player> = mutableListOf(),
    var membersId : MutableList<String> = mutableListOf()
) : Parcelable {
    fun toGameTeam() : GameTeam {
        return GameTeam(
                name = name,
                acronym = acronym,
                teamId = id
        )
    }
}


@Parcelize
data class Player(
    var id : String = "",
    var userId : String? = null,
    var teamId : String? = null,
    var name : String = "",
    var nickname : String? = null,
    var number : Int = -1,
    var image: String? = null,
    //var hitStat: HitterBox = HitterBox(name = name, playerId = id)
    //var pitchStat: PitcherBox = PitcherBox()
) : Parcelable