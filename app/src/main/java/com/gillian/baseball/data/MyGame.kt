package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyGame (
        val isHome : Boolean? = null,
        val game : Game
) : Parcelable
