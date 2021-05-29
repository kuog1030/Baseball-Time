package com.gillian.baseball.login

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Team
import kotlinx.android.parcel.Parcelize

object UserManager {

    val prefs = BaseballApplication.instance.getSharedPreferences("Baseball", Context.MODE_PRIVATE)
    var userId: String
        get() {
            return ( prefs.getString("user", "a5n9NOoKGAsLbEb0oyH8")!! )
        }
        set(id) {
            prefs.edit().putString("user", id).apply()
        }

    var playerId: String
        get() {
            return ( prefs.getString("player", "kWOBrCuTTB7e4ncz6xb4")!! )
        }
        set(id) {
            prefs.edit().putString("player", id).apply()
        }

    var teamId: String
        get() {
            return ( prefs.getString("team", "pmZ3TH0DWvlaXJ3qtAIy")!! )
        }
        set(id) {
            prefs.edit().putString("team", id).apply()
        }

    //var teamId: String = "qyjO8rziwuMz7O0oUfIo"
    var teamImage: String = "https://api.appworks-school.tw/assets/201807242234/0.jpg"
    var teamName : String = "Android"
    var teamAcronym : String = "AD"

    var team : Team? = Team(id = teamId, image = teamImage, name = teamName, acronym = teamAcronym)
}