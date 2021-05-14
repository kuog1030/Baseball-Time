package com.gillian.baseball.game

enum class EventType(val number : Int, val letter: String) {
    SINGLE(1, "1B"),
    DOUBLE(2, "2B"),
    TRIPLE(3, "3B"),
    HOMERUN(4, "HR"),
    HITBYPITCH(5, "HBP"),
    DROPPEDTHIRD(7, ""),
    RUN(11, "R"), //得分
    HIT(12, "H"),
    ERROR(13, "E"),
    STEALBASE(21, "SB"),
    INNINGSPITCHED(50, "IP")
}