package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class PitcherBox(
//        var ip : Float = 0F,
//        var hit : Int = 0,
//        var run : Int = 0,
//        var earnedRuns : Int = 0,
//        var bb : Int = 0,
//        var so : Int = 0,
//        var homerun : Int = 0,
//
//)

@Parcelize
data class PitcherBox(
        var name : String? = "",
        var order : Int = -1,
        val playerId : String = "",
        var inningsPitched : Float = 0F,
        var hit : Int = 0,
        var run : Int = 0,
        var earnedRuns : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var homerun : Int = 0,
        var totalStrike: Int = 0,
        var totalBall: Int = 0

) : Parcelable {

    fun addNewBox(newBox: PitcherBox) {
        this.apply {
            inningsPitched += newBox.inningsPitched
            run += newBox.run
            hit += newBox.hit
            earnedRuns += newBox.earnedRuns
            baseOnBalls += newBox.baseOnBalls
            strikeOut += newBox.strikeOut
            homerun += newBox.homerun
            totalStrike += newBox.totalStrike
            totalBall += newBox.totalBall
        }
    }
}