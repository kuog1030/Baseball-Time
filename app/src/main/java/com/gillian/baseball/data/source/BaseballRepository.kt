package com.gillian.baseball.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*

interface BaseballRepository {

    suspend fun initTeamAndPlayer(team: Team, player: Player)

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player)

    suspend fun createGame(game: Game) : Result<Game>

    suspend fun scheduleGame(game: Game) : Result<Boolean>

    suspend fun uploadImage(uri: Uri) : Result<String>

    suspend fun updateGame(game: Game) : Result<Boolean>

    suspend fun updateGameNote(gameId: String, note: String) : Result<Boolean>

    suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean>

    suspend fun updatePlayerInfo(player: Player) : Result<Boolean>

    suspend fun updateHitStat(playerId: String, hitterBox: HitterBox) : Result<Boolean>

    suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox) : Result<Boolean>

    suspend fun getAllGames(teamId: String) : Result<List<Game>>

    suspend fun getAllGamesCard(teamId: String) : Result<List<GameCard>>

    suspend fun getTeam(teamId: String): MutableLiveData<Team>

    suspend fun getTeam2(teamId: String): Result<Team>

    suspend fun getOnePlayer(playerId: String) : Result<Player>

    suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>>

    suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>>

    suspend fun getGameBox(gameId: String) : Result<Box>

    suspend fun getMyGameStat(gameId: String, isHome: Boolean) : Result<MyStatistic>

    suspend fun getBothGameStat(gameId: String) : Result<Statistic>

    suspend fun sendEvent(gameId: String, event: Event)

    suspend fun deletePlayer(playerId: String) : Result<Boolean>

}