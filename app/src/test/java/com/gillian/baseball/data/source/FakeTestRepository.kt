package com.gillian.baseball.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.google.firebase.auth.FirebaseUser

class FakeTestRepository : BaseballRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun findUser(userId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpUser(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun initTeamAndPlayer(team: Team, player: Player): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchPlayer(playerId: String): Result<List<Player>> {
        TODO("Not yet implemented")
    }

    override suspend fun registerPlayer(playerId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun createTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun createPlayer(player: Player): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun createGame(game: Game): Result<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun scheduleGame(game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uri: Uri): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGame(game: Game): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGameStatus(gameId: String, status: GameStatus): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGameNote(gameId: String, note: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlayerInfo(player: Player): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTeamInfo(team: Team): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHitStat(playerId: String, hitterBox: HitterBox): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun clearMyStat(playerId: String, myName: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getLiveEvents(gameId: String): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLiveGame(gameId: String): MutableLiveData<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGames(teamId: String): Result<List<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLiveGamesCard(): Result<List<GameCard>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGamesCard(teamId: String): Result<List<GameCard>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeam(teamId: String): Result<Team> {
        TODO("Not yet implemented")
    }

    override suspend fun getOnePlayer(playerId: String): Result<Player> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGame(gameId: String): Result<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyGameStat(gameId: String, isHome: Boolean): Result<MyStatistic> {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(gameId: String, event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlayer(playerId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGame(gameId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}