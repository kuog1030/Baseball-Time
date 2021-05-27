package com.gillian.baseball.data.source

import android.net.Uri
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

    override suspend fun uploadImage(uri: Uri): Result<String> {
        return remoteDataSource.uploadImage(uri)
    }

    override suspend fun updateGame(game: Game): Result<Boolean> {
        return remoteDataSource.updateGame(game)
    }

    override suspend fun updateGameNote(gameId: String, note: String): Result<Boolean> {
        return remoteDataSource.updateGameNote(gameId, note)
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> {
        return remoteDataSource.updateGameBox(gameId, box)
    }

    override suspend fun updatePlayerInfo(player: Player): Result<Boolean> {
        return remoteDataSource.updatePlayerInfo(player)
    }

    override suspend fun updateHitStat(playerId: String, hitterBox: HitterBox): Result<Boolean> {
        return remoteDataSource.updateHitStat(playerId, hitterBox)
    }

    override suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox): Result<Boolean> {
        return remoteDataSource.updatePitchStat(playerId, pitcherBox)
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

    override suspend fun getOnePlayer(playerId: String): Result<Player> {
        return remoteDataSource.getOnePlayer(playerId)
    }

    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> {
        return remoteDataSource.getTeamPlayer(teamId)
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> {
        return remoteDataSource.getTeamEventPlayer(teamId)
    }

    override suspend fun getGameBox(gameId: String): Result<Box> {
        return remoteDataSource.getGameBox(gameId)
    }

    override suspend fun getMyGameStat(gameId: String, isHome: Boolean): Result<MyStatistic> {
        return remoteDataSource.getMyGameStat(gameId, isHome)
    }

    override suspend fun getBothGameStat(gameId: String): Result<Statistic> {
        return remoteDataSource.getBothGameStat(gameId)
    }

    override suspend fun sendEvent(gameId: String, event: Event) {
        return remoteDataSource.sendEvent(gameId, event)
    }
}