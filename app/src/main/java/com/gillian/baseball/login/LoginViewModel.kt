package com.gillian.baseball.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.Team
import com.gillian.baseball.data.User
import com.gillian.baseball.data.source.BaseballRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: BaseballRepository) : ViewModel() {

    val firebaseUser = MutableLiveData<FirebaseUser>()

    private val _userExist = MutableLiveData<String>()

    val userExist : LiveData<String>
        get() = _userExist

    private val _signUpResult = MutableLiveData<User>()

    val signUpResult : LiveData<User>
        get() = _signUpResult

    private val _navigateToTeam = MutableLiveData<Team>()

    val navigateToTeam : LiveData<Team>
        get() = _navigateToTeam


    fun signInWithGoogle(token: String?) {
        Log.i("gillianlog", "sign in ing!")
        token?.let {
            viewModelScope.launch {
                val result = repository.signInWithGoogle(token)
                firebaseUser.value = when(result) {
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
    }

    // 要在這一步找User 有沒有過我的資料
    // find user query我的user id，
    // find user -> 回傳true 用原本的userId去找Player資料 -> 再找Team資料 -> 完成之後跳轉到team
    //           -> 回傳false 直接set -> 跳轉到login search
    fun searchIfUserExist() {
        firebaseUser.value?.let{
            viewModelScope.launch {
                val result = repository.findUser(it.uid)
                _userExist.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    else -> {
                        null
                    }
                }

            }
        }
    }

    fun newUserSignUp() {
        firebaseUser.value?.let {
            val user = User(email = it.email!!, id = it.uid, image = it.photoUrl.toString())
            viewModelScope.launch {
                val result = repository.signUpUser(user)
                _signUpResult.value = when (result) {
                    is Result.Success -> {
                        Log.i("gillianlog", "in viewmodel success")
                        result.data
                    }
                    else -> {
                        Log.i("gillianlog", "in viewmodel fail")
                        null
                    }
                }
            }
        }
    }

    // use UserManager.playerId to search for
    fun getTeamAndPlayer(playerId: String) {
        viewModelScope.launch {
            val getResult = repository.getTeamByPlayer(playerId)

            _navigateToTeam.value = when (getResult) {
                is Result.Success -> {
                    Log.i("gillian", "get team by player success")
                    getResult.data
                }
                else -> {
                    Log.i("gillianlog", "get team by player fail")
                    null
                }
            }

        }
    }


    // 如果已經授權過 拿到的firebase user會是一樣的


    fun onFirstLoginNavigated() {
        _signUpResult.value = null
    }

}