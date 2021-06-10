package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OnBaseInfo(
        val gameId: String,
        val inning: Int,
        val out: Int,
        val onClickPlayer: Int,
        val isDefence: Boolean,
        val pitcher: EventPlayer?,
        val baseList: List<EventPlayer?>
) : Parcelable