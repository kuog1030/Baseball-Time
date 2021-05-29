package com.gillian.baseball.game

enum class EventType(val number : Int, val letter: String, val isAtBat: Boolean, val isBatting: Boolean, val color: Int, val broadcast: String) {
    SINGLE(1, "1B", true, true, 0, "擊出一壘安打"),
    DOUBLE(2, "2B", true, true, 0, "擊出二壘安打"),
    TRIPLE(3, "3B", true, true, 0, "擊出三壘安打"),
    HOMERUN(4, "HR", true, true, 0, "擊出全壘打！"),
    HITBYPITCH(5, "HBP", false, true, 1, "觸身球保送"),
    ERRORONBASE(6, "E", true, true, 1, "因對手失誤上壘"),
    DROPPEDTHIRD(7, "KK", true, true, 1, "不死三振"),
    WALK(8, "BB", false, true, 2, "四壞球保送"),
    STRIKEOUT(9, "K", true, true, 1, "三振出局"),
    FIELDERCHOICE(10, "FC", true, true, 1, "野手選擇上壘"),
    RUN(11, "R", false, false, -1, "回壘得分"), //得分
    ERROR(13, "E", false, false, -1, "失誤"),
    GROUNDOUT(21, "GO", true, true, 1, "滾地球出局"),
    AIROUT(22, "AO", true, true, 1, "高飛球出局"),
    SACRIFICEFLY(23, "SF", false, true, 2, "高飛犧牲打"), // 高飛犧牲打
    SACRIFICEGO(24, "SAC", false, true, 2, "犧牲推進"), // 推進效果的犧牲打
    // 壘包上事件
    STEALBASE(31, "SB", false, false, -1, "盜壘成功"),
    PICKOFF(32, "PO", false, false, -1, "牽制出局"),
    INNINGSPITCHED(50, "IP", false, false, -1, "投球局數"),
    PINCHPITCHER(61, "", false, false, -1, "換投")
}