package com.gillian.baseball.data.source

import com.gillian.baseball.data.*

class DefaultBaseballRepository(private val remoteDataSource: BaseballDataSource,
                                private val localDataSource: BaseballDataSource
) : BaseballRepository {

    override suspend fun createTeam(team: Team) {
        return remoteDataSource.createTeam(team)
    }

    override suspend fun createPlayer(player: Player) {
        return remoteDataSource.createPlayer(player)
    }

    override suspend fun createGame(game: Game) {
        return remoteDataSource.createGame(game)
    }

    override suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer> {
        return remoteDataSource.getTeamPlayer(teamId)
    }

    override suspend fun sendEvent(event: Event) {
        return remoteDataSource.sendEvent(event)
    }
}