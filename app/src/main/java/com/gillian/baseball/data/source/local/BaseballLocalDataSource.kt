package com.gillian.baseball.data.source.local

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource
import com.google.firebase.auth.FirebaseUser

class BaseballLocalDataSource(val context: Context) : BaseballDataSource {

    override suspend fun signInWithGoogle(idToken: String) : Result<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun findUser(userId: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamByPlayer(playerId: String): Result<Team> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpUser(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun initTeamAndPlayer(team: Team, player: Player) : Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun createTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun createPlayer(player: Player) : Result<Boolean> {
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

    override suspend fun updateGameNote(gameId: String, note: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlayerInfo(player: Player): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHitStat(playerId: String, hitterBox: HitterBox): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTeam(teamName: String): Result<List<Team>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(gameId: String): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun getLiveEvents(gameId: String): MutableLiveData<List<Event>> {
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

//    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
//        TODO("Not yet implemented")
//    }

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

    override suspend fun getGameBox(gameId: String): Result<Box> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyGameStat(gameId: String, isHome: Boolean): Result<MyStatistic> {
        TODO("Not yet implemented")
    }

    override suspend fun getBothGameStat(gameId: String): Result<Statistic> {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(gameId: String, event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlayer(playerId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}