package com.gillian.baseball.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class GameCard(
        var id: String = "",
        val title: String = "",
        var place: String = "",
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
        get() = toDateString(date, 0)

    val dateStringShort: String
        get() = toDateString(date, 1)

    val dateStringTime: String
        get() = toDateString(date, 2)

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

    private fun toDateString(dateLong: Long, format: Int): String {
        lateinit var dateFormat : SimpleDateFormat
        if (format == 0) {
            dateFormat = SimpleDateFormat("yyyy-MM-dd EEE HH:mm", Locale.TAIWAN)
        } else if (format == 1) {
            dateFormat = SimpleDateFormat("yyyy-MM-dd EEE", Locale.TAIWAN)
        } else {
            dateFormat = SimpleDateFormat("MM-dd HH:mm", Locale.TAIWAN)
        }
        return dateFormat.format(Date(dateLong))
    }
}