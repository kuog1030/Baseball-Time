package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AtBase (
        var base: Int,
        val player: EventPlayer
) : Parcelable {
    val runnerString = "原本的${base}壘跑者${player.name}現在到了..."
}

data class OnBaseInfo(
        val inning: Int,
        val out: Int,
        val onClickPlayer: Int,
        val pitcher: EventPlayer?,
        val baseList: List<EventPlayer?>
)