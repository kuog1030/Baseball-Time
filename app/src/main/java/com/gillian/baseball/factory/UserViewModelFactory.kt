package com.gillian.baseball.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.User
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.loginflow.LoginCreateViewModel
import com.gillian.baseball.loginflow.LoginGetViewModel

class UserViewModelFactory constructor(
        private val repository: BaseballRepository,
        private val user: User
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(LoginCreateViewModel::class.java) ->
                        LoginCreateViewModel(repository, user)
                    isAssignableFrom(LoginGetViewModel::class.java) ->
                        LoginGetViewModel(repository, user)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}