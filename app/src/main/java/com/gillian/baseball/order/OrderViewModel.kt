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

    val selectedSideRadio = MutableLiveData<Int>()
    val gameTitle = MutableLiveData<String>()
    val awayTeamName = MutableLiveData<String>()
    val pitcher = MutableLiveData<String>()
    var startingPitcher : EventPlayer? = null

    var lineUp = mutableListOf<EventPlayer>()
    var pitcherList = mutableListOf<EventPlayer>()
    lateinit var myBench : MutableList<EventPlayer>

    val awayLineUp = mutableListOf<EventPlayer>()

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

    fun fillInfo() {
        gameCard?.let{
            selectedSideRadio.value = if (it.isHome) R.id.radio_order_home else R.id.radio_order_guest
            gameTitle.value = it.title
            awayTeamName.value = if (it.isHome) it.guestName else it.homeName
        }
    }

    fun getTeamPlayer() {

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
        Log.i("gillian", "pitcher $startingPitcher")
    }

    // check if all filled -> set up a game -> (either) create / update game in firebase
    //   -> set up game -> navigate to game
    fun checkIfAllFilled() {
        if (selectedSideRadio.value == null || gameTitle.value == null || awayTeamName.value == null || startingPitcher == null) {
            _errorMessage.value = getString(R.string.error_order)
        } else {
            _errorMessage.value = null
            setUpAGame()
        }
    }

    fun setUpAGame() {
        _errorMessage.value = null
        val readyToSent = Game(
                id = gameCard?.id ?: "",
                title = gameTitle.value!!,
                date = Calendar.getInstance().timeInMillis,
                place = gameCard?.place ?: "",
                recordedTeamId = UserManager.teamId,
                status = GameStatus.PLAYING.number)

        // TODO() 這樣對手的playerid都會一樣有點危險
        for (index in 1..minOf(9, lineUp.size)) {
            awayLineUp.add(EventPlayer(playerId = "$index", name = "第${index}棒", order = (index*100)))
            lineUp[index-1].order = index*100
        }

        if (startingPitcher == null) {
            pitcherList[0].order = 1
        }

        val myTeam = GameTeam(
                name = UserManager.teamName,
                acronym = UserManager.teamAcronym,
                teamId = UserManager.teamId,
                pitcher = startingPitcher ?: pitcherList[0],
                lineUp = lineUp.subList(0, minOf(9, lineUp.size))
        )

        // 先發九人以外的那些
        myBench = lineUp.subList(minOf(9, lineUp.size), lineUp.size)

        val awayTeam = GameTeam(
                name = awayTeamName.value!!,
                pitcher = EventPlayer(name = "對方投手", playerId = "10"),
                lineUp = awayLineUp
        )

        readyToSent.home = if (isHome) myTeam else awayTeam
        readyToSent.guest = if (isHome) awayTeam else myTeam

        if (gameCard == null) {
            createGameInFirebase(readyToSent)
        } else {
            updateGameInFirebase(readyToSent)
        }
    }

    fun createGameInFirebase(readyToSent: Game) {
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

    fun updateGameInFirebase(readyToSent: Game) {
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


    fun selectPitcher(position: Int) {
        startingPitcher = pitcherList[position]
        startingPitcher?.order = 1
    }


    fun showNewPlayerDialog() {
        _showNewPlayerDialog.value = true
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

    fun onNewPlayerDialogShowed() {
        _showNewPlayerDialog.value = null
    }

    fun refresh() {
        getTeamPlayer()
        startingPitcher = null
    }

}