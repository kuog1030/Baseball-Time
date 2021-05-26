package com.gillian.baseball.data.source.local

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
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

    override suspend fun createGame(game: Game) : Result<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun scheduleGame(game: Game) : Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uri: Uri): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGame(game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }
    
    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGames(teamId: String): Result<List<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGamesCard(teamId: String): Result<List<GameCard>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeam2(teamId: String): Result<Team> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHittersStat(gameId: String, teamId: String): Result<List<HitterBox>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGameBox(gameId: String): Result<Box> {
        TODO("Not yet implemented")
    }

    override suspend fun getGameStat(gameId: String, teamId: String): Result<MyStatistic> {
        TODO("Not yet implemented")
    }

//    override suspend fun getGameStat(gameId: String, teamId: String): Result<Statistic> {
//        TODO("Not yet implemented")
//    }

    override suspend fun sendEvent(gameId: String, event: Event) {
        TODO("Not yet implemented")
    }
}