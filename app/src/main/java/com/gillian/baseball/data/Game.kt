package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Game (
        var id: String = "",
        var title: String = "",
        var date: Long = -1,
        var place: String = "",
        var home: GameTeam = GameTeam(),
        var guest: GameTeam = GameTeam(),
        val box: Box = Box(),
        val note: String = "",
        val recordedTeamId : String = ""
) : Parcelable {

    fun toGameCard() : GameCard{
        return GameCard(
                id = id,
                title = title,
                place = place,
                date = date,
                isHome = (recordedTeamId == home.teamId),
                homeName = home.name,
                guestName = guest.name,
                homeScore = box.run[1],
                guestScore = box.run[0]
        )
    }
}
