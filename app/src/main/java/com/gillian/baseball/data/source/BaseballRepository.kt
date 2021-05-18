package com.gillian.baseball.data.source

import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game

interface BaseballRepository {

    suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer>

    suspend fun createGame(game: Game)

    suspend fun sendEvent(event: Event)

}