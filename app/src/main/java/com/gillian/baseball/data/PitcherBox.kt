package com.gillian.baseball.data

import android.os.Parcelable
import com.gillian.baseball.ext.toInningCount
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PitcherBox(
        var name : String? = "",
        var order : Int = -1,
        val playerId : String = "",
        var gamePlayed : Int = 0,
        var inningsPitched : Int = 0,
        var hit : Int = 0,
        var run : Int = 0,
        var error: Int = 0,
        var earnedRuns : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var homerun : Int = 0,
        var totalStrike: Int = 0,
        var totalBall: Int = 0

) : Parcelable {

    val inningsFormat : String
        get() = inningsPitched.toInningCount()

    val era : Float
        get() = myEra()

    val whip : Float
        get() = myWhip()

    val kNine: Float
        get() = myKNine()

    fun myEra() : Float {
        if (inningsPitched == 0) {
            return 0F
        } else {
            return (earnedRuns.toFloat() / inningsPitched * 27)
        }
    }

    fun myWhip() : Float {
        if (inningsPitched == 0) {
            return 0F
        } else {
            return ((hit + baseOnBalls) / inningsPitched.toFloat() * 3)
        }
    }

    fun myKNine() : Float {
        if (inningsPitched == 0) {
            return 0F
        } else {
            return (strikeOut.toFloat() / inningsPitched * 27)
        }
    }

    fun addNewBox(newBox: PitcherBox) {
        this.apply {
            name = newBox.name
            gamePlayed += 1
            inningsPitched += newBox.inningsPitched
            run += newBox.run
            hit += newBox.hit
            error += newBox.error
            earnedRuns += newBox.earnedRuns
            baseOnBalls += newBox.baseOnBalls
            strikeOut += newBox.strikeOut
            homerun += newBox.homerun
            totalStrike += newBox.totalStrike
            totalBall += newBox.totalBall
        }
    }
}