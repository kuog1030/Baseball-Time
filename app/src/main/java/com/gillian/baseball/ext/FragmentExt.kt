package com.gillian.baseball.ext

import androidx.fragment.app.Fragment
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.*
import com.gillian.baseball.factory.GameViewModelFactory
import com.gillian.baseball.factory.OnBaseViewModelFactory
import com.gillian.baseball.factory.TeamViewModelFactory
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

fun Fragment.getVmFactory(team: Team?): TeamViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return TeamViewModelFactory(repository, team)
}