package com.gillian.baseball.data.source.remote

import android.util.Log
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource

object BaseballRemoteDataSource : BaseballDataSource{

    override suspend fun createTeam(team: Team) {
        Log.i("remote", "create a team $team")
    }

    override suspend fun createPlayer(player: Player) {
        Log.i("remote", "create a player $player")
    }

    override suspend fun createGame(game: Game) {
        Log.i("remote", "game to be created $game")
    }

    override suspend fun getTeamPlayer(teamId: String): MutableList<Player> {
        Log.i("remote", "get mock team player")
        return  mutableListOf(
                Player(name = "Scolley", number = "15", image = "https://api.appworks-school.tw/assets/201807242234/0.jpg"),
                Player(name = "Gillian", number = "22", image = "https://api.appworks-school.tw/assets/201807242234/1.jpg"),
                Player(name = "Wency", number = "33"),
                Player(name = "Chloe", number = "79"),
                Player(name = "Peter", number = "1"),
                Player(name = "Wayne", number = "26"),
                Player(name = "Serena", number = "71"),
                Player(name = "Wenbin", number = "42"),
                Player(name = "Czerny", number = "18"),
                Player(name = "Sean", number = "66"),
                Player(name = "Shirney", number = "1")
        )
    }

    override suspend fun getTeamEventPlayer(teamId: String): MutableList<EventPlayer> {
        // MOCK DATA
        Log.i("remote", "get mock team EVENT player")
        return mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"),
                EventPlayer("0032","蘇智傑", "32"),
                EventPlayer("0013", "陳鏞基", "13"),
                EventPlayer("0077", "林安可", "77"),
                EventPlayer("0065", "陳重羽", "65"),
                EventPlayer("0064", "林靖凱", "64"),
                EventPlayer("0052", "張偉聖", "52"),
                EventPlayer("0031", "林岱安", "31"),
                EventPlayer("0039", "林祖傑", "39")
        )
    }

    override suspend fun sendEvent(event: Event) {
        Log.i("remote", "event to be sent $event")
    }
}