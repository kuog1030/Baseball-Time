package com.gillian.baseball.data

//data class HitterBox (
//        var atBat : Int = 0,
//        var run : Int = 0,
//        var hit : Int = 0,
//        var rbi : Int = 0,
//        var bb : Int = 0,
//        var so : Int = 0,
//        var sb: Int = 0
//)

data class HitterBox (
        var name : String? = "",
        val playerId : Long = 0L,
        var atBat : Int = 0,
        var run : Int = 0,
        var hit : Int = 0,
        var runsBattedIn : Int = 0,
        var baseOnBalls : Int = 0,
        var strikeOut : Int = 0,
        var stealBase: Int = 0
)

//
//data class PlayerBox(val isHitter: Boolean, val hitter: HitterBox, val pitcher: PitcherBox) {
//    val name = if (isHitter) hitter.name else pitcher.name
//    val abOrIp : Float = if (isHitter) hitter.atBat.toFloat() else pitcher.inningsPitched
//    val hit = if (isHitter) hitter.hit else pitcher.hit
//    val run = if (isHitter) hitter.run else pitcher.run
//}