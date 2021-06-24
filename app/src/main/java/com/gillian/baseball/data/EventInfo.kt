package com.gillian.baseball.data

data class EventInfo (
        val gameId: String,
        val atBaseList: MutableList<AtBase>,
        val isSafe: Boolean,
        val hitterPreEvent: Event,
        val isDefence: Boolean,
        val baseForThreeOut: Int? = null,
        val onField: List<EventPlayer>
)