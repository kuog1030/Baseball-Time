package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyGame (
        val isHome : Boolean,
        val benchPlayer: MutableList<EventPlayer>,
        val game : Game
) : Parcelable
