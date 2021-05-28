package com.gillian.baseball.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.User
import com.gillian.baseball.data.source.BaseballRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: BaseballRepository) : ViewModel() {

    val firebaseUser = MutableLiveData<FirebaseUser>()

    val signUpResult = MutableLiveData<Boolean>()


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

    fun singUpUserInFirebase() {
        firebaseUser.value?.let{
            val user = User(email = it.email!!)
            viewModelScope.launch {
                val result = repository.signUpUser(user)
                signUpResult.value = when (result) {
                    is Result.Success -> {
                        //TODO() 跳轉
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

}