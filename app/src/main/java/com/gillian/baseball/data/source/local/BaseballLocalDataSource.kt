package com.gillian.baseball.data.source.local

import android.content.Context
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource

class BaseballLocalDataSource(val context: Context) : BaseballDataSource {

    override suspend fun initTeamAndPlayer(team: Team, player: Player) {
        TODO("Not yet implemented")
    }

    override suspend fun createTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun createPlayer(player: Player) {
        TODO("Not yet implemented")
    }

    override suspend fun createGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamPlayer(teamId: String): MutableList<Player> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamEventPlayer(teamId: String): MutableList<EventPlayer> {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(event: Event) {
        TODO("Not yet implemented")
    }
}