package com.gillian.baseball.data.source.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballDataSource
import com.gillian.baseball.login.UserManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object BaseballRemoteDataSource : BaseballDataSource {

    private const val PLAYERS = "players"
    private const val TEAMS = "teams"
    private const val MEMBERS_LIST = "membersId"
    private const val TEAMID = "teamId"


    override suspend fun initTeamAndPlayer(team: Team, player: Player) {
        val teams = FirebaseFirestore.getInstance().collection(TEAMS)
        val document = teams.document()

        val players = FirebaseFirestore.getInstance().collection(PLAYERS)
        val playerDocument = players.document()


        team.id = document.id
        player.id = playerDocument.id
        player.userId = UserManager.userId
        team.membersId.add(playerDocument.id)
        UserManager.teamId = document.id

        document.set(team).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                player.teamId = UserManager.teamId
                playerDocument.set(player).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("remote", "first create team $team and player $player success")
                    } else {
                        task.exception?.let {
                            Log.i("remote", "first create a player fail ${it.message}")
                        }
                    }
                }
            } else {
                task.exception?.let {
                    Log.i("remote", "first create a team fail ${it.message}")
                }
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

    override suspend fun createGame(game: Game) {
        Log.i("remote", "game to be created $game")
    }

    override suspend fun getTeam(teamId: String): MutableLiveData<Team> {
        val team = MutableLiveData<Team>()
        FirebaseFirestore.getInstance().collection(TEAMS)
            .document(teamId)
            .get()
            .addOnCompleteListener {task ->
                task.result?.let{
                    team.value = it.toObject(Team::class.java)
                }
            }
        return team
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

    override suspend fun getTeamEventPlayer(teamId: String): MutableList<EventPlayer> {
        // MOCK DATA
        Log.i("remote", "get mock team EVENT player")
        return mutableListOf<EventPlayer>(EventPlayer("0024", "陳傑憲", "24"),
                EventPlayer("0032", "蘇智傑", "32"),
                EventPlayer("0013", "陳鏞基", "13"),
                EventPlayer("0077", "林安可", "77"),
                EventPlayer("0065", "陳重羽", "65"),
                EventPlayer("0064", "林靖凱", "64"),
                EventPlayer("0052", "張偉聖", "52"),
                EventPlayer("0031", "林岱安", "31"),
                EventPlayer("0039", "林祖傑", "39")
        )
    }

    override suspend fun sendEvent(event: Event) {
        Log.i("remote", "event to be sent $event")
    }
}