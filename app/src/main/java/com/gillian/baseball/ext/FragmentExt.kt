package com.gillian.baseball.ext

import androidx.fragment.app.Fragment
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.Game
import com.gillian.baseball.factory.GameViewModelFactory
import com.gillian.baseball.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(game: Game?): GameViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return GameViewModelFactory(repository, game)
}