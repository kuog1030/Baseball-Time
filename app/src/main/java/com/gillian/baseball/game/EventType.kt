package com.gillian.baseball.game

enum class EventType(val number : Int, val letter: String, val isAtBat: Boolean) {
    SINGLE(1, "1B", true),
    DOUBLE(2, "2B", true),
    TRIPLE(3, "3B", true),
    HOMERUN(4, "HR", true),
    HITBYPITCH(5, "HBP", false),
    ERRORONBASE(6, "E", true),
    DROPPEDTHIRD(7, "KK", true),
    WALK(8, "BB", false),
    STRIKEOUT(9, "K", true),
    FIELDERCHOICE(10, "FC", true), // 野手選擇
    RUN(11, "R", false), //得分
    HIT(12, "H", false),
    ERROR(13, "E", false),
    GROUNDOUT(21, "GO", true),
    AIROUT(22, "AO", true),
    SACRIFICEFLY(23, "SF", false), // 高飛犧牲打
    SACRIFICEGO(24, "SAC", false), // 推進效果的犧牲打
    // 壘包上事件
    STEALBASE(31, "SB", false),
    PICKOFF(32, "PO", false),  // 牽制出局
    INNINGSPITCHED(50, "IP", false)
}