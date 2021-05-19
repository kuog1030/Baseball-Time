package com.gillian.baseball.data

//data class PitcherBox(
//        var ip : Float = 0F,
//        var hit : Int = 0,
//        var run : Int = 0,
//        var earnedRuns : Int = 0,
//        var bb : Int = 0,
//        var so : Int = 0,
//        var homerun : Int = 0,
//
//)

data class PitcherBox(
        var name : String? = "",
        val playerId : Long = 0L,
        var inningsPitched : Float = 0F,
        var hit : Int = 0,
        var run : Int = 0,
        var earnedRuns : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var homerun : Int = 0,

)