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
