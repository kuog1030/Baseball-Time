package com.gillian.baseball.data.source

import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*

interface BaseballDataSource {

    suspend fun initTeamAndPlayer(team: Team, player: Player)

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game)

    suspend fun getTeam(teamId: String): MutableLiveData<Team>

    suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>>

    suspend fun getTeamEventPlayer(teamId: String): MutableList<EventPlayer>

    suspend fun sendEvent(event: Event)

}