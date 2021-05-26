package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
        var id : String = "",
        var userId : String? = null,
        var teamId : String? = null,
        var name : String = "",
        var nickname : String? = null,
        var number : Int = -1,
        var image: String? = null,
        var hitStat: HitterBox = HitterBox(),
        var pitchStat: PitcherBox = PitcherBox()
) : Parcelable