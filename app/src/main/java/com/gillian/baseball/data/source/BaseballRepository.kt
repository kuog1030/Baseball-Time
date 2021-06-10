package com.gillian.baseball.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.google.firebase.auth.FirebaseUser

interface BaseballRepository {

    suspend fun signInWithGoogle(idToken: String) : Result<FirebaseUser>

    suspend fun findUser(userId: String) : Result<Boolean>

    suspend fun signUpUser(user: User): Result<User>

    suspend fun initTeamAndPlayer(team: Team, player: Player) : Result<Boolean>

    suspend fun searchPlayer(playerId: String) : Result<List<Player>>

    suspend fun registerPlayer(playerId: String) : Result<Boolean>

    suspend fun createTeam(team: Team)

    suspend fun createPlayer(player: Player) : Result<Boolean>

    suspend fun createGame(game: Game) : Result<Game>

    suspend fun scheduleGame(game: Game) : Result<Boolean>

    suspend fun uploadImage(uri: Uri) : Result<String>

    suspend fun updateGame(game: Game) : Result<Boolean>

    suspend fun updateGameStatus(gameId: String, status: GameStatus) : Result<Boolean>

    suspend fun updateGameNote(gameId: String, note: String) : Result<Boolean>

    suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean>

    suspend fun updatePlayerInfo(player: Player) : Result<Boolean>

    suspend fun updateTeamInfo(team: Team) : Result<Boolean>

    suspend fun updateHitStat(playerId: String, hitterBox: HitterBox) : Result<Boolean>

    suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox) : Result<Boolean>

    //suspend fun searchTeam(teamName: String) : Result<List<Team>>

    suspend fun getAllEvents(gameId: String): List<Event>

    suspend fun getLiveEvents(gameId: String): MutableLiveData<List<Event>>

    suspend fun getLiveGame(gameId: String): MutableLiveData<Game>

    suspend fun getAllGames(teamId: String) : Result<List<Game>>

    suspend fun getAllLiveGamesCard() : Result<List<GameCard>>

    suspend fun getAllGamesCard(teamId: String) : Result<List<GameCard>>

    //suspend fun getTeam(teamId: String): MutableLiveData<Team>

    suspend fun getTeam(teamId: String): Result<Team>

    suspend fun getOnePlayer(playerId: String) : Result<Player>

    suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>>

    suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>>

    suspend fun getGame(gameId: String) : Result<Game>

    suspend fun getMyGameStat(gameId: String, isHome: Boolean) : Result<MyStatistic>

    suspend fun getBothGameStat(gameId: String) : Result<Statistic>

    suspend fun sendEvent(gameId: String, event: Event)

    suspend fun deletePlayer(playerId: String) : Result<Boolean>

    suspend fun deleteGame(gameId: String) : Result<Boolean>

}