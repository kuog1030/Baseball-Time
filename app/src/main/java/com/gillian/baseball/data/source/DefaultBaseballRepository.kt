package com.gillian.baseball.data.source

import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game

class DefaultBaseballRepository(private val remoteDataSource: BaseballDataSource,
                                private val localDataSource: BaseballDataSource
) : BaseballRepository {

    override suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer> {
        return remoteDataSource.getTeamPlayer(teamId)
    }

    override suspend fun createGame(game: Game) {
        return remoteDataSource.createGame(game)
    }

    override suspend fun sendEvent(event: Event) {
        return remoteDataSource.sendEvent(event)
    }
}