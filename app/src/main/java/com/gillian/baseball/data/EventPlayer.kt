package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventPlayer(
        var playerId: String = "",
        var order: Int = -1,
        val name: String = "",
        val number: String = ""
) : Parcelable