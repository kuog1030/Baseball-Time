package com.gillian.baseball.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch
import java.util.*

class OrderViewModel(private val repository: BaseballRepository, private val gameCard: GameCard?) : ViewModel() {

    private val totalOrder = 9

    val selectedSideRadio = MutableLiveData<Int>()

    val gameTitle = MutableLiveData<String>()

    val awayTeamName = MutableLiveData<String>()

    val toggleBroadcast = MutableLiveData<Boolean>(false)

    var startingPitcher: EventPlayer? = null

    // line up which is movable
    var lineUp = mutableListOf<EventPlayer>()

    // the pitcher list for choosing starting pitcher
    var pitcherList = mutableListOf<EventPlayer>()

    lateinit var myBench: MutableList<EventPlayer>

    val submitList = MutableLiveData<Boolean>()

    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_order_home -> true
            else -> false
        }

    private val _setUpGame = MutableLiveData<Game>()

    val setUpGame: LiveData<Game>
        get() = _setUpGame

    private val _navigateToGame = MutableLiveData<MyGame>()

    val navigateToGame: LiveData<MyGame>
        get() = _navigateToGame

    private val _showNewPlayerDialog = MutableLiveData<Boolean>()

    val showNewPlayerDialog: LiveData<Boolean>
        get() = _showNewPlayerDialog

    // error handle for blank input
    private val _emptyInfo = MutableLiveData<String>()

    val emptyInfo: LiveData<String>
        get() = _emptyInfo

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status


    init {
        fillInfo()
        getTeamPlayer()
    }

    // fill the scheduled game info
    private fun fillInfo() {
        gameCard?.let {
            selectedSideRadio.value = if (it.isHome) R.id.radio_order_home else R.id.radio_order_guest
            gameTitle.value = it.title
            awayTeamName.value = if (it.isHome) it.guestName else it.homeName
        }
    }

    private fun getTeamPlayer() {
        _status.value = LoadStatus.LOADING
        viewModelScope.launch {
            val result = repository.getTeamEventPlayer(UserManager.teamId)
            lineUp = when (result) {
                is Result.Success -> {
                    _status.value = LoadStatus.DONE
                    pitcherList = result.data
                    result.data
                }
                is Result.Fail -> {
                    _status.value = LoadStatus.ERROR
                    mutableListOf()
                }
                is Result.Error -> {
                    _status.value = LoadStatus.ERROR
                    mutableListOf()
                }
                else -> {
                    _status.value = LoadStatus.ERROR
                    mutableListOf()
                }
            }
            submitList.value = true
        }
    }

    // check if all filled -> set up a game -> (either) create / update game in firebase
    //   -> set up game -> navigate to game
    fun checkIfAllFilled() {
        if (selectedSideRadio.value == null || gameTitle.value.isNullOrEmpty() || awayTeamName.value.isNullOrEmpty() || startingPitcher == null) {
            _emptyInfo.value = getString(R.string.error_order)
        } else {
            _emptyInfo.value = null
            setUpAGame()
        }
    }


    private fun setUpAGame() {
        // pitcher could also be in batting line up, thus separate pitcher and the same person in line
        val copyPitcher = EventPlayer(
                order = 1,
                playerId = startingPitcher?.playerId ?: pitcherList[0].playerId,
                name = startingPitcher?.name ?: pitcherList[0].name,
                number = startingPitcher?.number ?: pitcherList[0].number
        )

        // players other than starting players
        myBench = lineUp.subList(minOf(totalOrder, lineUp.size), lineUp.size)
        val myTeam = createMyGameTeam(copyPitcher)

        val awayLineUp = createAwayLineUp()
        val awayTeam = GameTeam(
                name = awayTeamName.value!!,
                pitcher = EventPlayer(name = "對方投手", playerId = "10"),
                lineUp = awayLineUp
        )

        val readyToSend = Game(
                id = gameCard?.id ?: "",
                place = gameCard?.place ?: "",
                title = gameTitle.value!!,
                date = Calendar.getInstance().timeInMillis,
                recordedTeamId = UserManager.teamId,
                home = if (isHome) myTeam else awayTeam,
                guest = if (isHome) awayTeam else myTeam,
                status = if (toggleBroadcast.value == true) GameStatus.PLAYING.number else GameStatus.PLAYINGPRIVATE.number)

        if (gameCard == null) {
            createGameInFirebase(readyToSend)
        } else {
            updateGameInFirebase(readyToSend)
        }
    }

    private fun createAwayLineUp(): MutableList<EventPlayer> {
        val result = mutableListOf<EventPlayer>()
        for (index in 1..minOf(totalOrder, lineUp.size)) {
            result.add(EventPlayer(playerId = "", name = "第${index}棒", order = (index * 100)))
            lineUp[index - 1].order = index * 100
        }
        return result
    }

    private fun createMyGameTeam(startingPitcher: EventPlayer): GameTeam {
        return GameTeam(
                name = UserManager.team?.name ?: "",
                acronym = UserManager.team?.acronym ?: "",
                image = UserManager.team?.image ?: "",
                teamId = UserManager.teamId,
                pitcher = startingPitcher,
                lineUp = lineUp.subList(0, minOf(totalOrder, lineUp.size))
        )
    }


    private fun createGameInFirebase(readyToSent: Game) {
        viewModelScope.launch {
            val result = repository.createGame(readyToSent)

            _setUpGame.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    result.data
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    null
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    private fun updateGameInFirebase(readyToSent: Game) {
        viewModelScope.launch {
            val result = repository.updateGame(readyToSent)

            _setUpGame.value = when (result) {
                is Result.Success -> {
                    _errorMessage.value = null
                    readyToSent
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                    null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                    null
                }
                else -> {
                    _errorMessage.value = getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    fun navigateToGame(game: Game) {
        if (_status.value == LoadStatus.DONE) {
            _setUpGame.value = null
            _navigateToGame.value = MyGame(isHome = isHome, game = game, benchPlayer = myBench)
        }
    }

    fun onGameNavigated() {
        _navigateToGame.value = null
    }

    // for pitcher spinner selection
    fun selectPitcher(position: Int) {
        startingPitcher = pitcherList[position]
        startingPitcher?.order = 1
    }

    fun showNewPlayerDialog() {
        _showNewPlayerDialog.value = true
    }

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        getTeamPlayer()
        startingPitcher = null
    }

}