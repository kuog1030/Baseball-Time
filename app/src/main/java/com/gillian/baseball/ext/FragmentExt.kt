package com.gillian.baseball.ext

import androidx.fragment.app.Fragment
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.*
import com.gillian.baseball.data.EventInfo
import com.gillian.baseball.factory.*

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(myGame: MyGame): GameViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return GameViewModelFactory(repository, myGame)
}

fun Fragment.getVmFactory(gameCard: GameCard?): GameCardViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return GameCardViewModelFactory(repository, gameCard)
}

fun Fragment.getVmFactory(onBaseInfo: OnBaseInfo): OnBaseViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return OnBaseViewModelFactory(repository, onBaseInfo)
}

fun Fragment.getVmFactory(team: Team): TeamViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return TeamViewModelFactory(repository, team)
}

fun Fragment.getVmFactory(eventInfo: EventInfo): EventViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return EventViewModelFactory(repository, eventInfo)
}

fun Fragment.getVmFactory(user: User): UserViewModelFactory {
    val repository = (requireContext().applicationContext as BaseballApplication).repository
    return UserViewModelFactory(repository, user)
}