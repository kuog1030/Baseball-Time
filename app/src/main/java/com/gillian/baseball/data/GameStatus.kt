package com.gillian.baseball.data

enum class GameStatus(val number: Int) {
    SCHEDULED(0),
    PLAYING(1),
    PLAYINGPRIVATE(2),
    FINAL(3),
    FINALWITHSTAT(4)
}