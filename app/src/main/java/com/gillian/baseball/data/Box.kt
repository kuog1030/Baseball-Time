package com.gillian.baseball.data

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Box(
        val score: MutableList<Int> = mutableListOf(0),
        val run: MutableList<Int> = mutableListOf(0, 0),
        val hit: MutableList<Int> = mutableListOf(0, 0),
        val error: MutableList<Int> = mutableListOf(0, 0)
) : Parcelable {

    fun toBoxViewList(): List<BoxView> {
        var inning = 1
        var isOdd = false
        val result = mutableListOf<BoxView>()

        // home play one inning less than guest team
        if (this.score.size % 2 == 1) {
            isOdd = true
            this.score.add(-1)
        }

        for (i in 1..this.score.size step 2) {
            result.add(
                    BoxView(
                            type = "$inning",
                            guest = this.score[i - 1].toString(),
                            home = this.score[i].toString()
                    )
            )
            inning += 1
        }

        if (isOdd) result[result.size - 1].home = "X"

        if (this.score.size < 18) {
            val end = inning
            for (i in end..9) {
                result.add(BoxView(type = "$inning", guest = "-", home = "-"))
                inning += 1
            }
        }

        result.add(BoxView(type = "R", guest = this.run[0].toString(), home = this.run[1].toString()))
        result.add(BoxView(type = "H", guest = this.hit[0].toString(), home = this.hit[1].toString()))
        result.add(BoxView(type = "E", guest = this.error[0].toString(), home = this.error[1].toString()))

        return result
    }
}