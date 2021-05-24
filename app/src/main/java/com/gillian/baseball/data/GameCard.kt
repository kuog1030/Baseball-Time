package com.gillian.baseball.data

import java.text.SimpleDateFormat
import java.util.*

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
        var guestScore: Int = -1,
        val myScore: List<String> = listOf()
) {
    val dateString: String
        get() = toDateString(date)

    private fun toDateString(dateLong: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN)
        return dateFormat.format(Date(dateLong))
    }
}