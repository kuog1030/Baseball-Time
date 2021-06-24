package com.gillian.baseball.login

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.Player
import com.gillian.baseball.data.Team
import kotlinx.android.parcel.Parcelize

object UserManager {

    private val prefs = BaseballApplication.instance.getSharedPreferences("Baseball", Context.MODE_PRIVATE)

    var userId: String
        get() {
            return ( prefs.getString("user", "")!! )
        }
        set(id) {
            prefs.edit().putString("user", id).apply()
        }

    var playerId: String
        get() {
            return ( prefs.getString("player", "")!! )
        }
        set(id) {
            prefs.edit().putString("player", id).apply()
        }

    var teamId: String
        get() {
            return ( prefs.getString("team", "")!! )
        }
        set(id) {
            prefs.edit().putString("team", id).apply()
        }

    var team : Team? = null
}