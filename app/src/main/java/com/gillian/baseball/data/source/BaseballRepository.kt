package com.gillian.baseball.data.source

import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*

interface BaseballRepository {

    suspend fun initTeamAndPlayer(team: Team, player: Player)

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game)

    suspend fun scheduleGame(game: Game) : Result<Boolean>

    suspend fun getTeam(teamId: String): MutableLiveData<Team>

    suspend fun getTeam2(teamId: String): Result<Team>

    suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>>

    suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>>

    suspend fun sendEvent(gameId: String, event: Event)

}