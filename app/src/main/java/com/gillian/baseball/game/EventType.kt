package com.gillian.baseball.game

enum class EventType(val number : Int, val letter: String, val isAtBat: Boolean, val isBatting: Boolean, val color: Int) {
    SINGLE(1, "1B", true, true, 0),
    DOUBLE(2, "2B", true, true, 0),
    TRIPLE(3, "3B", true, true, 0),
    HOMERUN(4, "HR", true, true, 0),
    HITBYPITCH(5, "HBP", false, true, 1),
    ERRORONBASE(6, "E", true, true, 1),
    DROPPEDTHIRD(7, "KK", true, true, 1),
    WALK(8, "BB", false, true, 2),
    STRIKEOUT(9, "K", true, true, 1),
    FIELDERCHOICE(10, "FC", true, true, 1), // 野手選擇
    RUN(11, "R", false, false, -1), //得分
    ERROR(13, "E", false, false, -1),
    GROUNDOUT(21, "GO", true, true, 1),
    AIROUT(22, "AO", true, true, 1),
    SACRIFICEFLY(23, "SF", false, true, 2), // 高飛犧牲打
    SACRIFICEGO(24, "SAC", false, true, 2), // 推進效果的犧牲打
    // 壘包上事件
    STEALBASE(31, "SB", false, false, -1),
    PICKOFF(32, "PO", false, false, -1),  // 牽制出局
    INNINGSPITCHED(50, "IP", false, false, -1)
}