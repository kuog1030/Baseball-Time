package com.gillian.baseball.data.source.local

import android.content.Context
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.source.BaseballDataSource

class BaseballLocalDataSource(val context: Context) : BaseballDataSource {

    override suspend fun getTeamPlayer(teamId: String): MutableList<EventPlayer> {
        TODO("Not yet implemented")
    }

    override suspend fun createGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(event: Event) {
        TODO("Not yet implemented")
    }
}