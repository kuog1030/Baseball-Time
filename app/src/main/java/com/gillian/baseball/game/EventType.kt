package com.gillian.baseball.game

enum class EventType(val number : Int, val letter: String) {
    SINGLE(1, "1B"),
    DOUBLE(2, "2B"),
    TRIPLE(3, "3B"),
    HOMERUN(4, "HR"),
    HITBYPITCH(5, "HBP"),
    DROPPEDTHIRD(7, "KK"),
    WALK(8, "BB"),
    STRIKEOUT(9, "K"),
    FIELDERCHOICE(10, "FC"),
    RUN(11, "R"), //得分
    HIT(12, "H"),
    ERROR(13, "E"),
    GROUNDOUT(21, "GO"),
    AIROUT(22, "AO"),
    SACRIFICEFLY(23, "SF"),
    STEALBASE(31, "SB"),
    INNINGSPITCHED(50, "IP")
}