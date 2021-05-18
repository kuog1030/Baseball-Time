package com.gillian.baseball.data.source.remote

import android.util.Log
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource

object BaseballRemoteDataSource : BaseballDataSource{

    override suspend fun createTeam(team: Team) {
        Log.i("remote", "create a team")
    }

    override suspend fun createPlayer(player: Player) {
        Log.i("remote", "create a player")
    }

    override suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer> {
        // MOCK DATA
        Log.i("remote", "get team player")
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

    override suspend fun createGame(game: Game) {
        Log.i("remote", "game to be created $game")
    }

    override suspend fun sendEvent(event: Event) {
        Log.i("remote", "event to be sent $event")
    }
}