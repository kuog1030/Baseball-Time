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

    override suspend fun createGame(game: Game) : Result<Game> {
        return remoteDataSource.createGame(game)
    }

    override suspend fun scheduleGame(game: Game) : Result<Boolean> {
        return remoteDataSource.scheduleGame(game)
    }

    override suspend fun updateGame(game: Game): Result<Boolean> {
        return remoteDataSource.updateGame(game)
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> {
        return remoteDataSource.updateGameBox(gameId, box)
    }

    override suspend fun getAllGames(teamId: String): Result<List<Game>> {
        return remoteDataSource.getAllGames(teamId)
    }

    override suspend fun getAllGamesCard(teamId: String): Result<List<GameCard>> {
        return remoteDataSource.getAllGamesCard(teamId)
    }

    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
        return remoteDataSource.getTeam(teamId)
    }

    override suspend fun getTeam2(teamId: String): Result<Team> {
        return remoteDataSource.getTeam2(teamId)
    }

    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> {
        return remoteDataSource.getTeamPlayer(teamId)
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> {
        return remoteDataSource.getTeamEventPlayer(teamId)
    }

    override suspend fun getHittersStat(gameId: String, teamId: String): Result<List<HitterBox>> {
        return remoteDataSource.getHittersStat(gameId, teamId)
    }

    override suspend fun getGameBox(gameId: String): Result<Box> {
        return remoteDataSource.getGameBox(gameId)
    }

    override suspend fun getGameStat(gameId: String, teamId: String): Result<Statistic> {
        return remoteDataSource.getGameStat(gameId, teamId)
    }

    override suspend fun sendEvent(gameId: String, event: Event) {
        return remoteDataSource.sendEvent(gameId, event)
    }
}