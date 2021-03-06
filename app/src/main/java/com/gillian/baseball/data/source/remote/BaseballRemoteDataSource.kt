package com.gillian.baseball.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource
import com.gillian.baseball.ext.*
import com.gillian.baseball.game.EventType
import com.gillian.baseball.login.UserManager
import com.google.common.io.Files.getFileExtension
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object BaseballRemoteDataSource : BaseballDataSource {

    private val auth = Firebase.auth

    // collections name
    private const val PLAYERS = "players"
    private const val TEAMS = "teams"
    private const val GAMES = "games"
    private const val USERS = "users"

    // sub collection
    private const val PLAYS = "plays"

    // field name
    private const val USERID = "userId"
    private const val TEAMID = "teamId"
    private const val PLAYERID = "playerId"
    private const val NOTE = "note"
    private const val NAME = "name"
    private const val NICKNAME = "nickname"
    private const val ACRONYM = "acronym"
    private const val NUMBER = "number"
    private const val IMAGE = "image"
    private const val HITSTAT = "hitStat"
    private const val PITCHSTAT = "pitchStat"
    private const val RECORDED = "recordedTeamId"
    private const val STATUS = "status"

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> = suspendCoroutine { continuation ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        continuation.resume(Result.Success(user!!))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error sign in with google. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("sign in fail"))
                    }
                }
    }

    override suspend fun findUser(userId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(USERS)
                .document(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // has user
                        val user = task.result!!
                        val userExist = task.result!!.exists()
                        if (userExist) {
                            UserManager.userId = user.get("id").toString()
                            UserManager.playerId = user.get(PLAYERID).toString()
                            UserManager.teamId = user.get(TEAMID).toString()
                        }
                        continuation.resume(Result.Success(userExist))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error find user. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("find user fail"))
                    }
                }
    }

    override suspend fun signUpUser(user: User): Result<User> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(USERS)
                .document(user.id)
                .set(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        UserManager.userId = user.id
                        continuation.resume(Result.Success(user))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error sign up user. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("sign up fail"))
                    }
                }
    }

    override suspend fun initTeamAndPlayer(team: Team, player: Player): Result<Boolean> = suspendCoroutine { continuation ->
        val teams = FirebaseFirestore.getInstance().collection(TEAMS)
        val document = teams.document()

        val players = FirebaseFirestore.getInstance().collection(PLAYERS)
        val playerDocument = players.document()

        team.id = document.id
        player.id = playerDocument.id
        player.userId = UserManager.userId

        UserManager.teamId = document.id
        UserManager.playerId = playerDocument.id

        document.set(team).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                player.teamId = UserManager.teamId

                playerDocument.set(player).addOnCompleteListener { playerTask ->
                    if (playerTask.isSuccessful) {
                        UserManager.team = team

                        // update user team id and player id
                        FirebaseFirestore.getInstance().collection(USERS)
                                .document(UserManager.userId)
                                .update("teamId", team.id, "playerId", player.id)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        updateTask.exception?.let {
                                            Log.w("gillian", "init update user fail ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        Log.i("gillian", "init update user fail")
                                        continuation.resume(Result.Fail("initTeamAndPlayer_update_user fail"))
                                    }
                                }

                    } else {
                        playerTask.exception?.let {
                            Log.w("gillian", "init create a player fail ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        Log.i("gillian", "init create player fail")
                        continuation.resume(Result.Fail("initTeamAndPlayer_player fail"))
                    }
                }
            } else {
                task.exception?.let {
                    continuation.resume(Result.Error(it))
                    Log.w("gillian", "init create a team exception ${it.message}")
                }
                Log.i("gillian", "init create team fail")
                continuation.resume(Result.Fail("initTeamAndPlayer_team fail"))
            }
        }

    }


    override suspend fun searchPlayer(playerId: String): Result<List<Player>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val playerList = mutableListOf<Player>()

                        for (document in task.result!!) {
                            if (document["id"].toString().contains(playerId)) {
                                playerList.add(document.toObject(Player::class.java))
                            }
                        }
                        continuation.resume(Result.Success(playerList))

                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("search player id fail"))
                    }
                }
    }


    override suspend fun registerPlayer(playerId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .update(USERID, UserManager.userId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error register player. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("register player fail"))
                    }
                }
    }

    override suspend fun scheduleGame(game: Game): Result<Boolean> = suspendCoroutine { continuation ->
        val games = FirebaseFirestore.getInstance().collection(GAMES)
        val document = games.document()

        game.id = document.id

        document.set(game)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("schedule a game fail"))
                    }
                }
    }

    override suspend fun createTeam(team: Team) {
        val teams = FirebaseFirestore.getInstance().collection(TEAMS)
        val document = teams.document()

        team.id = document.id
        document.set(team).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("gillian", "create a team $team success")
            } else {
                task.exception?.let {
                    Log.i("gillian", "create a team fail ${it.message}")
                }
            }
        }
    }

    override suspend fun createPlayer(player: Player): Result<Boolean> = suspendCoroutine { continuation ->
        val players = FirebaseFirestore.getInstance().collection(PLAYERS)
        val document = players.document()

        player.id = document.id
        player.teamId = UserManager.teamId

        document.set(player)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error creating new player. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("create new player fail"))
                    }
                }
    }

    // only be used when fast start up a game. will return the unique document id
    override suspend fun createGame(game: Game): Result<Game> = suspendCoroutine { continuation ->
        val games = FirebaseFirestore.getInstance().collection(GAMES)
        val document = games.document()

        game.id = document.id

        document.set(game).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Result.Success(game))
            } else {
                task.exception?.let {
                    Log.i("gillian", "create a team fail ${it.message}")
                    continuation.resume(Result.Error(it))
                }
                continuation.resume(Result.Fail("fast create a game fail"))
            }
        }
    }

    override suspend fun uploadImage(uri: Uri): Result<String> = suspendCoroutine { continuation ->
        val storageReference = FirebaseStorage.getInstance()
        val filename = UUID.randomUUID().toString()
        val fileReference = storageReference.reference.child(
                "images/$filename"
        )
        fileReference.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    fileReference.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(task.result.toString()))
                    }
                }
    }

    override suspend fun updateGame(game: Game): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(game.id)
                .set(game)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error updating game. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game fail"))
                    }

                }
    }

    override suspend fun updatePlayerInfo(player: Player): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(player.id)
                .update(
                        NAME, player.name,
                        NICKNAME, player.nickname,
                        IMAGE, player.image,
                        NUMBER, player.number
                )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error updating player info. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update player info fail"))
                    }
                }
    }

    override suspend fun updateTeamInfo(team: Team): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(TEAMS)
                .document(team.id)
                .update(
                        NAME, team.name,
                        ACRONYM, team.acronym,
                        IMAGE, team.image
                )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error updating team info. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update team info fail"))
                    }
                }
    }


    override suspend fun updateHitStat(playerId: String, hitterBox: HitterBox): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val player = task.result!!.toObject(Player::class.java)
                        val oldBox = player!!.hitStat
                        oldBox.addNewBox(hitterBox)
                        FirebaseFirestore.getInstance().collection(PLAYERS)
                                .document(playerId)
                                .update(HITSTAT, oldBox)
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task2.exception?.let {

                                            Log.w("gillian", "[${this::class.simpleName}] Error updating hit stat. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail("update hit stat fail"))
                                    }
                                }
                    } else {
                        continuation.resume(Result.Fail("update hit stat fail"))
                    }
                }
    }

    override suspend fun updatePitchStat(playerId: String, pitcherBox: PitcherBox): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val player = task.result!!.toObject(Player::class.java)
                        val oldBox = player!!.pitchStat
                        oldBox.addNewBox(pitcherBox)
                        FirebaseFirestore.getInstance().collection(PLAYERS)
                                .document(playerId)
                                .update(PITCHSTAT, oldBox)
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task2.exception?.let {

                                            Log.w("gillian", "[${this::class.simpleName}] Error updating pitch stat. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail("update pitch sta fail"))
                                    }
                                }
                    } else {
                        continuation.resume(Result.Fail("update pitch sta fail"))
                    }
                }
    }

    override suspend fun updateGameStatus(gameId: String, status: GameStatus): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .update(STATUS, status.number)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error updating game status. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game status fail"))
                    }
                }
    }

    override suspend fun updateGameNote(gameId: String, note: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .update(NOTE, note,
                        STATUS, GameStatus.FINALWITHSTAT.number)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error updating game note. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game note fail"))
                    }

                }
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .update("box", box)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error updating game box. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game box fail"))
                    }

                }
    }

    override suspend fun clearMyStat(playerId: String, myName: String): Result<Boolean> = suspendCoroutine { continuation ->
        val newHitStat = HitterBox(playerId = playerId, name = myName)
        val newPitchStat = PitcherBox(playerId = playerId, name = myName)

        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .update(HITSTAT, newHitStat,
                        PITCHSTAT, newPitchStat)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error clearing my stat. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        continuation.resume(Result.Fail("clear my stat fail"))
                    }
                }
    }

    override suspend fun getAllGames(teamId: String): Result<List<Game>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(GAMES)
                .whereEqualTo(RECORDED, UserManager.teamId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<Game>()
                        for (document in task.result!!) {
                            Log.i("gillian", " ${document.id} -> ${document.data}")
                            result.add(document.toObject(Game::class.java))
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all games fail"))
                    }
                }
    }


    override suspend fun getAllLiveGamesCard(): Result<List<GameCard>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(GAMES)
                .whereEqualTo(STATUS, GameStatus.PLAYING.number) // GameStatus.PLAYING.number = 1
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<GameCard>()
                        for (document in task.result!!) {
                            val game = document.toObject(Game::class.java).toGameCard()
                            if (game.date >= (Calendar.getInstance().timeInMillis - TimeUnit.DAYS.toMillis(3))) {
                                result.add(game)
                            }
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all live games fail"))
                    }
                }
    }

    override suspend fun getAllGamesCard(teamId: String): Result<List<GameCard>> = suspendCoroutine { continuation ->
        val games = FirebaseFirestore.getInstance().collection(GAMES)
        games.whereEqualTo(RECORDED, teamId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<GameCard>()
                        for (oneGame in task.result!!) {
                            result.add(oneGame.toObject(Game::class.java).toGameCard())
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all games card fail"))
                    }
                }
    }


    override suspend fun getTeam(teamId: String): Result<Team> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(TEAMS)
                .document(teamId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result!!.toObject(Team::class.java)
                        continuation.resume(Result.Success(result!!))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get team player suspend coroutine fail"))
                    }
                }
    }

    override suspend fun getOnePlayer(playerId: String): Result<Player> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .document(playerId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null) {
                            val result = task.result!!.toObject(Player::class.java)
                            continuation.resume(Result.Success(result!!))
                        } else {
                            continuation.resume(Result.Fail("get one player fail"))
                        }
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error getting one player. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get one player fail"))
                    }
                }
    }


    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .whereEqualTo(TEAMID, teamId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<Player>()
                        for (document in task.result!!) {
                            result.add(document.toObject(Player::class.java))
                        }
                        result.sortBy { it.number }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get team player fail"))
                    }
                }
    }

    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .whereEqualTo(TEAMID, teamId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<EventPlayer>()
                        for (document in task.result!!) {
                            result.add(EventPlayer(playerId = document["id"].toString(), name = document[NAME].toString(), number = document[NUMBER].toString()))
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get team event player fail"))
                    }
                }
    }

    override suspend fun getGame(gameId: String): Result<Game> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.data != null) {
                        val gameResult = documents.toObject(Game::class.java)
                        // TODO()????????????error handle?
                        continuation.resume(Result.Success(gameResult!!))
                    } else {
                        continuation.resume(Result.Fail("get game fail"))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
    }


    override suspend fun getMyGameStat(gameId: String, isHome: Boolean): Result<MyStatistic> = suspendCoroutine { continuation ->
        val theGame = FirebaseFirestore.getInstance().collection(GAMES).document(gameId)

        theGame.collection(PLAYS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventList = mutableListOf<Event>()
                        if (task.result != null) {
                            for (document in task.result!!) {
                                eventList.add(document.toObject(Event::class.java))
                            }
                            val result = eventList.toMyGameStat(isHome)
                            continuation.resume(Result.Success(result))
                        } else {
                            continuation.resume(Result.Fail("get all stats fail"))
                        }
                    } else {
                        task.exception?.let {
                            Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all stats fail"))
                    }
                }
    }


    override suspend fun sendEvent(gameId: String, event: Event) {
        val theGame = FirebaseFirestore.getInstance().collection(GAMES).document(gameId)
        val document = theGame.collection(PLAYS).document()

        event.id = document.id
        event.time = Calendar.getInstance().timeInMillis

        document.set(event).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("gillian", "send a event ${event.toLiveString()}")
            } else {
                task.exception?.let {
                    Log.i("gillian", "send a event fail ${it.message}")
                }
            }
        }
    }

    override suspend fun getLiveEvents(gameId: String): MutableLiveData<List<Event>> {
        var isInit = true
        val liveEvent = MutableLiveData<List<Event>>()
        val eventList = mutableListOf<Event>()

        FirebaseFirestore.getInstance().collection(GAMES).document(gameId)
                .collection(PLAYS)
                .addSnapshotListener { documents, error ->
                    error?.let {
                        Log.w("gillian", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                        return@addSnapshotListener
                    }
                    for (dc in documents!!.documentChanges) {
                        // EventType.RUN == 11
                        if (dc.document["result"].toString() != "11") {
                            eventList.add(0, dc.document.toObject(Event::class.java))
                        }
                    }
                    if (isInit) {
                        eventList.sortByDescending { it.time }
                        isInit = false
                    }
                    liveEvent.value = eventList
                }
        return liveEvent
    }

    override suspend fun getLiveGame(gameId: String): MutableLiveData<Game> {
        val liveData = MutableLiveData<Game>()

        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        Log.w("gillian", "[${this::class.simpleName}] Error getting game document. ${it.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        liveData.value = snapshot.toObject(Game::class.java)
                    }

                }
        return liveData
    }

    override suspend fun deletePlayer(playerId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error deleting player. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("delete player fail"))
                    }
                }

    }

    override suspend fun deleteGame(gameId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("gillian", "[${this::class.simpleName}] Error deleting schedule game. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("delete schedule game fail"))
                    }
                }

    }

    override suspend fun deleteUser(userId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(USERS)
            .document(userId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    FirebaseFirestore.getInstance().collection(PLAYERS)
                        .document(UserManager.playerId)
                        .update(USERID, null)
                        .addOnCompleteListener { playerTask ->
                            if (playerTask.isSuccessful) {
                                continuation.resume(Result.Success(true))
                            } else {
                                playerTask.exception?.let {
                                    Log.w("gillian", "[${this::class.simpleName}] Error removing player user id. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail("remove player user id"))
                            }
                        }
                } else {
                    task.exception?.let {
                        Log.w("gillian", "[${this::class.simpleName}] Error deleting user. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("delete user fail"))
                }
            }

    }

}