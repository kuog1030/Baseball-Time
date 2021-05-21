package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventPlayer(
        var playerId: String = "",
        val name: String = "",
        val number: String = "",
        var pinch: EventPlayer? = null
) : Parcelable