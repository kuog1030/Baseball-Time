package com.gillian.baseball.ext

import androidx.fragment.app.Fragment
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.OnBaseInfo
import com.gillian.baseball.factory.GameViewModelFactory
import com.gillian.baseball.factory.OnBaseViewModelFactory
import com.gillian.baseball.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(myGame: MyGame): GameViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return GameViewModelFactory(repository, myGame)
}

fun Fragment.getVmFactory(onBaseInfo: OnBaseInfo): OnBaseViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return OnBaseViewModelFactory(repository, onBaseInfo)
}