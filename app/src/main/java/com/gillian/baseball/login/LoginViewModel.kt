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

    private val _userExist = MutableLiveData<Boolean>()

    val userExist : LiveData<Boolean>
        get() = _userExist

    val showLoginPage = MutableLiveData<Boolean>(true)

    private val _initTeam = MutableLiveData<Team>()

    val initTeam : LiveData<Team>
        get() = _initTeam


    fun fetchTeam(showLogin: Boolean = false) {
        showLoginPage.value = showLogin
        viewModelScope.launch {
            val result = repository.getTeam(UserManager.teamId)
            _initTeam.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    // google login firebase
    fun signInWithGoogle(token: String?) {
        token?.let {
            viewModelScope.launch {
                val result = repository.signInWithGoogle(token)
                firebaseUser.value = when(result) {
                    is Result.Success -> {
                        searchIfUserExist(result.data.uid)
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


    // UserManager user Id, player Id, team Id will be set if find user return true
    fun searchIfUserExist(uid: String) {
            viewModelScope.launch {
                val result = repository.findUser(uid)
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

    fun createNewUser(firebaseUser: FirebaseUser) : User {
        return User(email = firebaseUser.email!!,
                id = firebaseUser.uid,
                image = firebaseUser.photoUrl.toString())
    }

}