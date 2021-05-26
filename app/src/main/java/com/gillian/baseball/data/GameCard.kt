package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class GameCard(
        var id: String = "",
        val title: String = "",
        val place: String = "",
        var date: Long = -1,
        var status: Int = 0,
        var isHome: Boolean = false,
        var homeName: String = "",
        var guestName: String = "",
        var homeScore: Int = -1,
        var guestScore: Int = -1
        //val myScore: List<String> = listOf()
) : Parcelable {
    val dateString: String
        get() = toDateString(date)

    val gameResult : GameResult
        get() {
            if (homeScore > guestScore) {
                return (if (isHome) GameResult.WIN else GameResult.LOSE)
            } else if (homeScore == guestScore) {
                return (GameResult.TIE)
            } else {
                return (if (isHome) GameResult.LOSE else GameResult.WIN)
            }
        }

    private fun toDateString(dateLong: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN)
        return dateFormat.format(Date(dateLong))
    }
}