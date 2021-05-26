package com.gillian.baseball.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*

interface BaseballDataSource {

    suspend fun initTeamAndPlayer(team: Team, player: Player)

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game) : Result<Game>

    suspend fun scheduleGame(game: Game) : Result<Boolean>

    suspend fun uploadImage(uri: Uri) : Result<String>

    suspend fun updateGame(game: Game) : Result<Boolean>

    suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean>

    suspend fun getAllGames(teamId: String) : Result<List<Game>>

    suspend fun getAllGamesCard(teamId: String) : Result<List<GameCard>>

    suspend fun getTeam(teamId: String): MutableLiveData<Team>

    suspend fun getTeam2(teamId: String): Result<Team>

    suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>>

    suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>>

    suspend fun getHittersStat(gameId: String, teamId: String) : Result<List<HitterBox>>

    suspend fun getGameBox(gameId: String) : Result<Box>

    //suspend fun getGameStat(gameId: String, teamId: String) : Result<Statistic>
    suspend fun getGameStat(gameId: String, teamId: String) : Result<MyStatistic>

    suspend fun sendEvent(gameId: String, event: Event)

}