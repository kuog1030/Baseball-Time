package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyGame (
        val isHome : Boolean,
        val game : Game
) : Parcelable
