package com.gillian.baseball.data.source

import com.gillian.baseball.data.*

interface BaseballDataSource {

    suspend fun initTeamAndPlayer(team: Team, player: Player)

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game)

    suspend fun getTeamPlayer(teamId: String): MutableList<Player>

    suspend fun getTeamEventPlayer(teamId: String): MutableList<EventPlayer>

    suspend fun sendEvent(event: Event)

}