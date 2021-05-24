package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Box (
        val score: MutableList<Int> = mutableListOf(0),
        val run: MutableList<Int> = mutableListOf(0, 0),
        val hit: MutableList<Int> = mutableListOf(0, 0),
        val error: MutableList<Int> = mutableListOf(0, 0)
) : Parcelable {

    // TODO 處理沒有九下之類的情況
    fun toBoxViewList() : List<BoxView> {
        var inning = 1
        val result = mutableListOf<BoxView>()

        for ( i in 1 .. this.score.size step 2 ) {
            result.add ( BoxView(type = "$inning",
                    guest = this.score[i-1].toString(),
                    home = this.score[i].toString()) )
            inning += 1
        }

        if ( this.score.size < 18 ) {
            val end = inning
            for (i in end .. 9) {
                result.add( BoxView(type = "$inning", guest = "-", home = "-"))
                inning += 1
            }
        }

        result.add( BoxView(type = "R", guest = this.run[0].toString(), home = this.run[1].toString()))
        result.add( BoxView(type = "H", guest = this.hit[0].toString(), home = this.hit[1].toString()))
        result.add( BoxView(type = "E", guest = this.error[0].toString(), home = this.error[1].toString()))

        return result
    }
}

//data class BoxView (
//        var type : String = "1",
//        var guest : String,
//        var home : String
//)