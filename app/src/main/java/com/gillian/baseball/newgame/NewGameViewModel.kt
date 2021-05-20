package com.gillian.baseball.newgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import kotlinx.coroutines.launch

class NewGameViewModel(val repository: BaseballRepository, private var myTeam: Team?) : ViewModel() {

    val homeName = MutableLiveData<String>()
    val guestName = MutableLiveData<String>()

    val awayName = MutableLiveData<String>()
    val gameTitle = MutableLiveData<String>()
    val gamePlace = MutableLiveData<String>()

    val selectedSideRadio = MutableLiveData<Int>(R.id.radio_new_game_home)

    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_new_game_home -> true
            else -> false
        }

    private val _teamPlayers = MutableLiveData<MutableList<EventPlayer>>()
    val teamPlayers: LiveData<MutableList<EventPlayer>>
        get() = _teamPlayers


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    // TODO()這邊不要hard coded!
    val gameDate = MutableLiveData<String>("選個日期吧")
    var gameDateLong: Long = -1L

    fun updateCard() {
        when (selectedSideRadio.value) {
            R.id.radio_new_game_home -> {
                homeName.value = UserManager.teamName
                guestName.value = awayName.value
            }
            else -> {
                homeName.value = awayName.value
                guestName.value = UserManager.teamName
            }

        }
    }

    fun formatDate(year: Int, month: Int, dayOfMonth: Int) {
        gameDate.value = "${year}-${month + 1}-${dayOfMonth}"
    }


    //偵測到teamplayer改變->變成string list
    fun getTeamPlayer() {

        viewModelScope.launch {

            val result = repository.getTeamEventPlayer(UserManager.teamId)

            _teamPlayers.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                is Result.Fail -> {
                    null
                }
                is Result.Error -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
    }

    fun convertToStringList(eventPlayerList: MutableList<EventPlayer>): List<String> {
        val result = mutableListOf<String>()
        for (player in eventPlayerList) {
            result.add("${player.number}號  ${player.name}")
        }
        return result
    }

    fun scheduleNewGame() {
        if (gameTitle.value.isNullOrEmpty() || gameDate.value.isNullOrEmpty() || gamePlace.value.isNullOrEmpty() || awayName.value.isNullOrEmpty() || gameDateLong == -1L) {
            _errorMessage.value = BaseballApplication.instance.getString(R.string.error_schedule_game)
        } else {
            _errorMessage.value = null

            if (myTeam == null) {
                myTeam = Team()
            }

            // pithcer and lineup not yet initialized
            val myGameTeam = GameTeam( name = myTeam!!.name, acronym = myTeam!!.acronym, teamId = myTeam!!.id)

            val game = Game(title = gameTitle.value!!, date = gameDateLong, place = gamePlace.value!!)

            if (isHome) {
                game.home = myGameTeam
                game.guest.name = awayName.value!!
            } else {
                game.guest = myGameTeam
                game.home.name = awayName.value!!
            }

            uploadNewGame(game)
        }
    }

    fun uploadNewGame(game: Game) {
        viewModelScope.launch {
            repository.createGame(game)
        }
    }





    //    fun navigateToGame() {
    //        val game = Game(
    //                title = gameTitle.value ?: "世界第一武道大會",
    //                date = Calendar.getInstance().timeInMillis,
    //                place = "")
    //
    //        for (index in 1..9) {
    //            awayLineUp.add(EventPlayer(name = "第${index}棒"))
    //        }
    //
    //        val myTeam = GameTeam(
    //                name = "Android",
    //                teamId = "999",
    //                pitcher = EventPlayer(name = pitcher.value ?: "古林睿揚", number = "19", userId = "0019"),
    //                lineUp = lineUp
    //        )
    //
    //        val awayTeam = GameTeam(
    //                name = awayTeamName.value ?: "iOS",
    //                pitcher = EventPlayer(name = "對方投手"),
    //                lineUp = awayLineUp
    //        )
    //
    //        game.home = if (isHome) myTeam else awayTeam
    //        game.guest = if (isHome) awayTeam else myTeam
    //
    //        viewModelScope.launch {
    //            repository.createGame(game)
    //        }
    //
    //
    //        _navigateToGame.value = MyGame(isHome = isHome, game = game)
    //    }





//    private val isHome: Boolean
//        get() = when (selectedSideRadio.value) {
//            R.id.radio_order_home -> true
//            else -> false
//        }

}