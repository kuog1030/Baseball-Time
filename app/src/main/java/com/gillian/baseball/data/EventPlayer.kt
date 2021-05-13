package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventPlayer(
    var userId: String = "",
    val name: String = "",
    val number: String = ""
) : Parcelable