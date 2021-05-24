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
import com.gillian.baseball.util.Util.getString
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewGameViewModel(val repository: BaseballRepository, private var myTeam: Team) : ViewModel() {

    val homeName = MutableLiveData<String>()
    val guestName = MutableLiveData<String>()

    val homeImage = MutableLiveData<String>()
    val guestImage = MutableLiveData<String>()

    val awayName = MutableLiveData<String>()
    val gameTitle = MutableLiveData<String>()
    val gamePlace = MutableLiveData<String>()
    val gameNote = MutableLiveData<String>()

    val selectedSideRadio = MutableLiveData<Int>(R.id.radio_new_game_home)

    private val _scheduleSuccess = MutableLiveData<Boolean>()

    val scheduleSuccess: LiveData<Boolean>
        get() = _scheduleSuccess

    private val isHome: Boolean
        get() = when (selectedSideRadio.value) {
            R.id.radio_new_game_home -> true
            else -> false
        }


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    val gameDate = MutableLiveData<String>(getString(R.string.select_a_date))
    private var gameDateLong: Long = -1L

    fun setUpDate(targetDate: Long) {
        gameDateLong = targetDate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN)
        gameDate.value = dateFormat.format(Date(targetDate))
    }


    fun updateCard() {
        when (selectedSideRadio.value) {
            R.id.radio_new_game_home -> {
                homeImage.value = UserManager.teamImage
                guestImage.value = ""
                homeName.value = UserManager.teamName
                guestName.value = awayName.value
            }
            else -> {
                homeName.value = awayName.value
                homeImage.value = ""
                guestName.value = UserManager.teamName
                guestImage.value = UserManager.teamImage
            }

        }
    }


    fun scheduleNewGame() {
        if (gameTitle.value.isNullOrEmpty() || gameDate.value.isNullOrEmpty() || gamePlace.value.isNullOrEmpty() || awayName.value.isNullOrEmpty() || gameDateLong == -1L) {
            _errorMessage.value = BaseballApplication.instance.getString(R.string.error_schedule_game)
        } else {
            _errorMessage.value = null

            // pithcer and lineup not yet initialized
            val myGameTeam = myTeam.toGameTeam()

            val game = Game(title = gameTitle.value!!, date = gameDateLong, place = gamePlace.value!!,
                    note = gameNote.value ?: "",
                    status = GameStatus.SCHEDULED.number,
                    recordedTeamId = UserManager.teamId)

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
            val result = repository.scheduleGame(game)
            _scheduleSuccess.value = when (result) {
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
                    _errorMessage.value = getString(R.string.return_nothing)
                    null
                }
            }
        }
    }

    fun onGameUploadedAndNavigated() {
        _scheduleSuccess.value = null
    }
}