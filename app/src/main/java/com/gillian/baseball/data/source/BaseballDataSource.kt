package com.gillian.baseball.data.source

import com.gillian.baseball.data.*

interface BaseballDataSource {

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game)

    suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer>

    suspend fun sendEvent(event: Event)

}