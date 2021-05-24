package com.gillian.baseball.login

import android.os.Parcelable
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Team
import kotlinx.android.parcel.Parcelize

object UserManager {

    // mock data
    var userId: String = "eNNuZztIQCNLwRI6sSZl"
    var playerId: String = "oZKeTwJrrDbrfFRt6TWI"
    var teamId: String = "qyjO8rziwuMz7O0oUfIo"
    var teamImage: String = "https://api.appworks-school.tw/assets/201807242234/0.jpg"
    var teamName : String = "Android"
    var teamAcronym : String = "AD"

    var team : Team? = Team(id = teamId, image = teamImage, name = teamName, acronym = teamAcronym)
}