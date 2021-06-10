package com.gillian.baseball.data

data class BasePlayer(
        var hitter: EventPlayer? = null,
        var first: EventPlayer? = null,
        var second: EventPlayer? = null,
        var third: EventPlayer? = null
)