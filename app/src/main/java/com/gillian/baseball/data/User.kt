package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var id: String = "",
    val email: String,
    val image: String = "",
    val playerId: String = "",
    val teamId: String = ""
) : Parcelable