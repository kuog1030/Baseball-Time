package com.gillian.baseball.data

enum class Position(val number: Int, val letter: String) {
    PITCHER(1, "P"),
    CATCHER(2, "C"),
    FIRSTBASE(3, "1B"),
    SECONDBASE(4, "2B"),
    THIRDBASE(5, "3B"),
    SHORTSTOP(6, "SS"),
    LEFTFIELD(7, "LF"),
    CENTERFIELD(8, "CF"),
    RIGHTFIELD(9, "RF")
}