package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class HitterBox (
//        var atBat : Int = 0,
//        var run : Int = 0,
//        var hit : Int = 0,
//        var rbi : Int = 0,
//        var bb : Int = 0,
//        var so : Int = 0,
//        var sb: Int = 0
//)

@Parcelize
data class HitterBox (
        var name : String? = "",
        val order: Int = -1,
        val playerId : String = "",
        var atBat : Int = 0,
        var run : Int = 0,
        var hit : Int = 0,
        var runsBattedIn : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var stealBase: Int = 0
) : Parcelable {

    fun addNewBox(newBox: HitterBox) {
        this.apply {
            atBat += newBox.atBat
            run += newBox.run
            hit += newBox.hit
            runsBattedIn += newBox.runsBattedIn
            baseOnBalls += newBox.baseOnBalls
            strikeOut += newBox.strikeOut
            stealBase += newBox.stealBase
        }
    }
}

//
//data class PlayerBox(val isHitter: Boolean, val hitter: HitterBox, val pitcher: PitcherBox) {
//    val name = if (isHitter) hitter.name else pitcher.name
//    val abOrIp : Float = if (isHitter) hitter.atBat.toFloat() else pitcher.inningsPitched
//    val hit = if (isHitter) hitter.hit else pitcher.hit
//    val run = if (isHitter) hitter.run else pitcher.run
//}