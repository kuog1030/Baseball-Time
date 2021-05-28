package com.gillian.baseball.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource
import com.gillian.baseball.ext.*
import com.gillian.baseball.login.UserManager
import com.google.common.io.Files.getFileExtension
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object BaseballRemoteDataSource : BaseballDataSource {

    private val auth = Firebase.auth

    private const val FIRESTORE_IMAGE = "images/"
    // collections name
    private const val PLAYERS = "players"
    private const val TEAMS = "teams"
    private const val GAMES = "games"
    private const val USERS = "users"
    // sub collection
    private const val PLAYS = "plays"

    private const val MEMBERS_LIST = "membersId"
    // field name
    private const val USERID = "userId"
    private const val TEAMID = "teamId"
    private const val NOTE = "note"
    private const val NAME = "name"
    private const val NICKNAME = "nickname"
    private const val NUMBER = "number"
    private const val IMAGE = "image"
    private const val HITSTAT = "hitStat"
    private const val PITCHSTAT = "pitchStat"
    private const val RECORDED = "recordedTeamId"

    override suspend fun signInWithGoogle(idToken: String) : Result<FirebaseUser> = suspendCoroutine { continuation ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("gillianlog", "signInWithCredential:success")
                    val user = auth.currentUser
                    continuation.resume(Result.Success(user!!))
                } else {
                    task.exception?.let{
                        Log.w("gillianlog", "signInWithCredential:failure", task.exception)
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    Log.i("gillianlog", "sign in fail")
                    continuation.resume(Result.Fail("sign in fail"))
                }
            }
    }

    override suspend fun signUpUser(user: User): Result<User> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(USERS)
        val document = users.document()

        user.id = document.id
        document.set(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("gillianlog", "create user success")
                continuation.resume(Result.Success(user))
            } else {
                task.exception?.let{
                    Log.w("gillianlog", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                Log.i("gillianlog", "create user")
                continuation.resume(Result.Fail("sign in fail"))
            }
        }
    }

    override suspend fun initTeamAndPlayer(team: Team, player: Player) : Result<Boolean> = suspendCoroutine { continuation ->
        val teams = FirebaseFirestore.getInstance().collection(TEAMS)
        val document = teams.document()

        val players = FirebaseFirestore.getInstance().collection(PLAYERS)
        val playerDocument = players.document()


        team.id = document.id
        player.id = playerDocument.id
        player.userId = UserManager.userId  // 這時候已經有user id了
        team.membersId.add(playerDocument.id)
        //TODO() 這邊要賦值嗎
        UserManager.teamId = document.id
        UserManager.playerId = playerDocument.id

        document.set(team).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                player.teamId = UserManager.teamId
                playerDocument.set(player).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        UserManager.team = team
                        continuation.resume(Result.Success(true))
                        Log.i("remote", "init create team $team and player $player success")
                    } else {
                        task.exception?.let {
                            Log.i("remote", "init create a player fail ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        Log.i("remote", "init create player fail")
                        continuation.resume(Result.Fail("initTeamAndPlayer_player fail"))
                    }
                }
            } else {
                task.exception?.let {
                    continuation.resume(Result.Error(it))
                    Log.i("remote", "init create a team exception ${it.message}")
                }
                Log.i("remote", "init create team fail")
                continuation.resume(Result.Fail("initTeamAndPlayer_team fail"))
            }
        }

    }


    override suspend fun scheduleGame(game: Game) : Result<Boolean> = suspendCoroutine { continuation ->

        val games = FirebaseFirestore.getInstance().collection(GAMES)
        val document = games.document()

        game.id = document.id
        //article.createdTime = Calendar.getInstance().timeInMillis

        document.set(game)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Result.Success(true))
                        } else {
                            task.exception?.let {

                                Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
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
                Log.i("remote", "create a team $team success")
            } else {
                task.exception?.let {
                    Log.i("remote", "create a team fail ${it.message}")
                }
            }
        }
    }

    override suspend fun createPlayer(player: Player) {
        val players = FirebaseFirestore.getInstance().collection(PLAYERS)
        val document = players.document()

        player.id = document.id
        player.teamId = UserManager.teamId

        document.set(player)
                .addOnSuccessListener {
                    // update teams member list
                    FirebaseFirestore.getInstance().collection(TEAMS)
                            .document(UserManager.teamId)
                            .update(MEMBERS_LIST, FieldValue.arrayUnion(player.id))
                            .addOnFailureListener {
                                Log.w("remote", "Error updating team member list $it")
                            }
                }
                .addOnFailureListener { exception ->
                    Log.w("remote", "Error creating new player $exception")
                }
    }

    // only be used when fast start up a game. will return the unique document id
    override suspend fun createGame(game: Game) : Result<Game> = suspendCoroutine {continuation ->
        //continuation.resume(Result.Success(game))
        val games = FirebaseFirestore.getInstance().collection(GAMES)
        val document = games.document()

        game.id = document.id

        document.set(game).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Result.Success(game))
            } else {
                task.exception?.let {
                    Log.i("remote", "create a team fail ${it.message}")
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
            .continueWithTask{task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileReference.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    continuation.resume(Result.Success(task.result.toString()))
                }
            }
    }

    override suspend fun updateGame(game: Game): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(game.id)
                .set(game)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error updating game. ${it.message}")
                            continuation.resume( Result.Error(it) )
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
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Log.w("remote", "[${this::class.simpleName}] Error updating player info. ${it.message}")
                        continuation.resume( Result.Error(it) )
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("update player info fail"))
                }
            }
    }

    override suspend fun updateHitStat(playerId: String, hitterBox: HitterBox) : Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
                .document(playerId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val player = task.result!!.toObject(Player::class.java)
                        val oldBox = player!!.hitStat
                        oldBox.addNewBox(hitterBox)
                        Log.i("gillian", "inside success")
                        FirebaseFirestore.getInstance().collection(PLAYERS)
                                .document(playerId)
                                .update(HITSTAT, oldBox)
                                .addOnCompleteListener{ task2 ->
                                    if (task2.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task2.exception?.let {

                                            Log.w("remote", "[${this::class.simpleName}] Error updating hit stat. ${it.message}")
                                            continuation.resume( Result.Error(it) )
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
                                .addOnCompleteListener{ task2 ->
                                    if (task2.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task2.exception?.let {

                                            Log.w("remote", "[${this::class.simpleName}] Error updating pitch stat. ${it.message}")
                                            continuation.resume( Result.Error(it) )
                                        }
                                        continuation.resume(Result.Fail("update pitch sta fail"))
                                    }
                                }
                    } else {
                        continuation.resume(Result.Fail("update pitch sta fail"))
                    }
                }
    }

    override suspend fun updateGameNote(gameId: String, note: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .update(NOTE, note)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error updating game note. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game note fail"))
                    }

                }
    }

    override suspend fun updateGameBox(gameId: String, box: Box): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .update("box", box,
                        "status", 2)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error updating game box. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("update game box fail"))
                    }

                }
    }

    override suspend fun getAllGames(teamId: String): Result<List<Game>>  = suspendCoroutine {continuation ->
        FirebaseFirestore.getInstance()
                .collection(GAMES)
                .whereEqualTo(RECORDED, UserManager.teamId)
                .get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<Game>()
                        for (document in task.result!!) {
                            Log.i("remote", " ${document.id} -> ${document.data}")
                            result.add(document.toObject(Game::class.java))
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all games fail"))
                    }
                }
    }

    override suspend fun getAllGamesCard(teamId: String): Result<List<GameCard>> = suspendCoroutine {continuation ->
        val games = FirebaseFirestore.getInstance().collection(GAMES)
        games.whereEqualTo(RECORDED, UserManager.teamId)
                .get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<GameCard>()
                        for (oneGame in task.result!!) {
                            result.add(oneGame.toObject(Game::class.java).toGameCard())
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all games card fail"))
                    }
                }
    }


//    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
//        val team = MutableLiveData<Team>()
//        FirebaseFirestore.getInstance().collection(TEAMS)
//            .document(teamId)
//            .get()
//            .addOnCompleteListener {task ->
//                task.result?.let{
//                    team.value = it.toObject(Team::class.java)
//                }
//            }
//        return team
//    }


    override suspend fun getTeam(teamId: String): Result<Team> = suspendCoroutine {continuation ->
        FirebaseFirestore.getInstance()
            .collection(TEAMS)
            .document(teamId)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    var result = task.result!!.toObject(Team::class.java)
                    continuation.resume(Result.Success(result!!))

                } else {
                    task.exception?.let {

                        Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume( Result.Error(it) )
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("get team player suspend coroutine fail"))
                }
            }
    }

    override suspend fun getOnePlayer(playerId: String): Result<Player> = suspendCoroutine {continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .document(playerId)
                .get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val result = task.result!!.toObject(Player::class.java)
                        continuation.resume(Result.Success(result!!))

                    } else {
                        task.exception?.let {
                            Log.w("remote", "[${this::class.simpleName}] Error getting one player. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get one player fail"))
                    }
                }
    }


    //TODO() 目前這個function沒有用到teamId欸
    override suspend fun getTeamPlayer(teamId: String): Result<MutableList<Player>> = suspendCoroutine {continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .whereEqualTo(TEAMID, UserManager.teamId)
                .get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<Player>()
                        for (document in task.result!!) {
                            Log.i("remote", " ${document.id} -> ${document.data}")
                            result.add(document.toObject(Player::class.java))
                        }
                        result.sortBy { it.number }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get team player fail"))
                    }
                }
    }

    //TODO() 目前這個function沒有用到teamId欸
    override suspend fun getTeamEventPlayer(teamId: String): Result<MutableList<EventPlayer>> = suspendCoroutine {continuation ->
        FirebaseFirestore.getInstance()
                .collection(PLAYERS)
                .whereEqualTo(TEAMID, UserManager.teamId)
                .get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val result = mutableListOf<EventPlayer>()
                        for (document in task.result!!) {
                            Log.i("remote", " ${document.id} -> ${document.data}")
                            result.add(EventPlayer(playerId = document["id"].toString(), name = document[NAME].toString(), number = document[NUMBER].toString()))
                        }
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume( Result.Error(it) )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get team event player fail"))
                    }
                }
    }

    override suspend fun getGameBox(gameId: String): Result<Box> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(GAMES)
                .document(gameId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        val gameResult = documents.toObject(Game::class.java)
                        continuation.resume(Result.Success(gameResult!!.box))
                    } else {
                        continuation.resume(Result.Fail("get game box fail"))
                    }
                }
                .addOnFailureListener{ exception ->
                    continuation.resume(Result.Error(exception))
                }
    }


    // TODO teamID是為了判斷我是is home嗎?
    override suspend fun getMyGameStat(gameId: String, isHome: Boolean): Result<MyStatistic> = suspendCoroutine { continuation ->
        val theGame = FirebaseFirestore.getInstance().collection(GAMES).document(gameId)

        theGame.collection(PLAYS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventList = mutableListOf<Event>()
                        for (document in task.result!!) {
                            eventList.add(document.toObject(Event::class.java))
                        }
                        //val result = eventList.toBothGameStat()
                        val result = eventList.toMyGameStat(isHome)
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all stats fail"))
                    }
                }
    }

    override suspend fun getBothGameStat(gameId: String): Result<Statistic> = suspendCoroutine { continuation ->
        val theGame = FirebaseFirestore.getInstance().collection(GAMES).document(gameId)

        theGame.collection(PLAYS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventList = mutableListOf<Event>()
                        for (document in task.result!!) {
                            eventList.add(document.toObject(Event::class.java))
                        }
                        val result = eventList.toBothGameStat()
                        continuation.resume(Result.Success(result))
                    } else {
                        task.exception?.let {

                            Log.w("remote", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("get all stats fail"))
                    }
                }
    }


    // TODO 回傳result?
    override suspend fun sendEvent(gameId: String, event: Event) {
        val theGame = FirebaseFirestore.getInstance().collection(GAMES).document(gameId)
        val document = theGame.collection(PLAYS).document()

        event.id = document.id
        event.time = Calendar.getInstance().timeInMillis

        document.set(event).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("remote", "send a event $event success")
            } else {
                task.exception?.let {
                    Log.i("remote", "send a event fail ${it.message}")
                }
            }
        }
    }

    override suspend fun deletePlayer(playerId: String): Result<Boolean> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PLAYERS)
            .document(playerId)
            .delete()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {

                        Log.w("remote", "[${this::class.simpleName}] Error deleting player. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("delete player fail"))
                }
            }

    }

}