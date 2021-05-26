package com.gillian.baseball.data

data class Statistic (
    val guestPitcher : List<PitcherBox>,
    val homePitcher : List<PitcherBox>,
    val guestHitter : List<HitterBox>,
    val homeHitter : List<HitterBox>
)

data class MyStatistic (
        val myPitcher: List<PitcherBox>,
        val myHitter: List<HitterBox>
)