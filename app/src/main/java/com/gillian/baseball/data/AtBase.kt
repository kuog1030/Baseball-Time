package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AtBase (
        val base: Int,
        val player: EventPlayer
) : Parcelable