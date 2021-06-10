package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HitterBox (
        var name : String? = "",
        val order: Int = -1,
        val playerId : String = "",
        var atBat : Int = 0,
        var run : Int = 0,
        var hit : Int = 0,
        var single: Int = 0,
        var douBle: Int = 0,
        var triple: Int = 0,
        var homerun: Int = 0,
        var runsBattedIn : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var stealBase: Int = 0,
        var sacrificeFly: Int = 0,
        var error: Int = 0
) : Parcelable {

    fun addNewBox(newBox: HitterBox) {
        this.apply {
            atBat += newBox.atBat
            single += newBox.single
            douBle += newBox.douBle
            triple += newBox.triple
            homerun += newBox.homerun
            run += newBox.run
            hit += newBox.hit
            runsBattedIn += newBox.runsBattedIn
            baseOnBalls += newBox.baseOnBalls
            strikeOut += newBox.strikeOut
            stealBase += newBox.stealBase
            sacrificeFly += newBox.sacrificeFly
            error += newBox.error
        }
    }

    // 打擊率
    fun myAverage() : Float? {
        if (atBat != 0) {
            return (hit / atBat.toFloat())
        } else {
            return null
        }
    }

    // 上壘率
    fun myObp() : Float? {
        if (atBat != 0) {
            return ((hit+baseOnBalls) / (atBat + baseOnBalls + sacrificeFly).toFloat())
        } else {
            return null
        }
    }

    // Slugging Percentage
    fun mySlg() : Float? {
        if (atBat != 0) {
            return ((single + (2*douBle) + (3*triple) + (4*homerun)) / atBat.toFloat())
        } else {
            return null
        }
    }

    // On-base Plus Slugging
    fun myOps() : Float? {
        if (atBat != 0) {
            return (myObp()!! + mySlg()!!)
        } else {
            return null
        }
    }
}