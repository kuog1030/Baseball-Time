package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HitterBox(
        var name: String? = "",
        val order: Int = -1,
        val playerId: String = "",
        var atBat: Int = 0,
        var run: Int = 0,
        var hit: Int = 0,
        var single: Int = 0,
        var douBle: Int = 0,
        var triple: Int = 0,
        var homerun: Int = 0,
        var gamePlayed: Int = 0,
        var runsBattedIn: Int = 0,
        var baseOnBalls: Int = 0,
        var hitByPitch: Int = 0,
        var strikeOut: Int = 0,
        var stealBase: Int = 0,
        var sacrificeHit: Int = 0,
        var sacrificeFly: Int = 0,
        var error: Int = 0
) : Parcelable {

    val avg: Float
        get() = myAverage()

    val obp: Float
        get() = myObp()

    val slg: Float
        get() = mySlg()

    fun addNewBox(newBox: HitterBox) {
        this.apply {
            name = newBox.name
            atBat += newBox.atBat
            single += newBox.single
            douBle += newBox.douBle
            triple += newBox.triple
            homerun += newBox.homerun
            run += newBox.run
            hit += newBox.hit
            gamePlayed += 1
            runsBattedIn += newBox.runsBattedIn
            baseOnBalls += newBox.baseOnBalls
            strikeOut += newBox.strikeOut
            stealBase += newBox.stealBase
            sacrificeHit += newBox.sacrificeHit
            sacrificeFly += newBox.sacrificeFly
            error += newBox.error
        }
    }

    private fun myAverage(): Float {
        return if (atBat != 0) {
            (hit / atBat.toFloat())
        } else {
            0F
        }
    }

    // On base Percentage
    private fun myObp(): Float {
        return if (atBat != 0) {
            // sacrifice hit is not counted into obp
            ((hit + baseOnBalls + hitByPitch) / (atBat + baseOnBalls + hitByPitch + sacrificeFly).toFloat())
        } else {
            0F
        }
    }

    // Slugging Percentage
    private fun mySlg(): Float {
        return if (atBat != 0) {
            ((single + (2 * douBle) + (3 * triple) + (4 * homerun)) / atBat.toFloat())
        } else {
            0F
        }
    }

    // On-base Plus Slugging
    fun myOps(): Float {
        return if (atBat != 0) {
            (obp + slg)
        } else {
            0F
        }
    }
}