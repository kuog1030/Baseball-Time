package com.gillian.baseball.data.source

import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*

class DefaultBaseballRepository(private val remoteDataSource: BaseballDataSource,
                                private val localDataSource: BaseballDataSource
) : BaseballRepository {

    override suspend fun initTeamAndPlayer(team: Team, player: Player) {
        return remoteDataSource.initTeamAndPlayer(team, player)
    }


    override suspend fun createTeam(team: Team) {
        return remoteDataSource.createTeam(team)
    }

    override suspend fun createPlayer(player: Player) {
        return remoteDataSource.createPlayer(player)
    }

    override suspend fun createGame(game: Game) {
        return remoteDataSource.createGame(game)
    }

    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
        return remoteDataSource.getTeam(teamId)
    }

    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> {
        return remoteDataSource.getTeamPlayer(teamId)
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> {
        return remoteDataSource.getTeamEventPlayer(teamId)
    }

    override suspend fun sendEvent(event: Event) {
        return remoteDataSource.sendEvent(event)
    }
}