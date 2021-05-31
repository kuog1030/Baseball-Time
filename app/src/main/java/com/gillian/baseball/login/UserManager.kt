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
            return ( prefs.getString("user", "")!! )
            //a5n9NOoKGAsLbEb0oyH8
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

    // TODO()這應該要刪掉
    var teamName: String
        get() {
            return ( prefs.getString("teamName", "")!! )
        }
        set(name) {
            prefs.edit().putString("teamName", name).apply()
        }

    var teamImage: String
        get() {
            return ( prefs.getString("teamImage", "")!! )
        }
        set(url) {
            prefs.edit().putString("teamImage", url).apply()
        }

    var teamAcronym: String
        get() {
            return ( prefs.getString("teamAcro", "")!! )
        }
        set(id) {
            prefs.edit().putString("teamAcro", id).apply()
        }

    var team : Team? = null
}