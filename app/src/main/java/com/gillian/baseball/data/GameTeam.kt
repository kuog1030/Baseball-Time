package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GameTeam (
        var name: String = "",
        val acronym : String = name,
        val teamId: String = "",
        var pitcher: EventPlayer = EventPlayer(),
        val lineUp: MutableList<EventPlayer> = mutableListOf()
) : Parcelable