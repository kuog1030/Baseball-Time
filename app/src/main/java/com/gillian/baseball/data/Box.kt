package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Box (
        val score: MutableList<Int> = mutableListOf(0),
        val run: MutableList<Int> = mutableListOf(0, 0),
        val hit: MutableList<Int> = mutableListOf(0, 0),
        val error: MutableList<Int> = mutableListOf(0, 0)
) : Parcelable