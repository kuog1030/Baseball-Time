package com.gillian.baseball.ext

import android.util.Log
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.data.PitcherBox
import com.gillian.baseball.game.EventType
import com.gillian.baseball.login.UserManager

fun List<Event>.toHitterBox(): List<HitterBox> {

    Log.i("gillian", "list size ${this.size}")
    val guestResult = mutableListOf<HitterBox>(HitterBox())
    val homeResult = mutableListOf<HitterBox>(HitterBox())

    fun updateBox(event: Event, box: HitterBox) {
        val type = event.result
        lateinit var targetEventType: EventType

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }

        // TODO() 之後得分的result要改掉，目前傳上去是0
        if (type == 0) targetEventType = EventType.RUN

        if (targetEventType.isAtBat) box.atBat += 1
        box.run += event.run
        box.runsBattedIn += event.rbi

        when (targetEventType) {
            EventType.SINGLE -> box.hit += 1
            EventType.DOUBLE -> box.hit += 1
            EventType.TRIPLE -> box.hit += 1
            EventType.HOMERUN -> box.hit += 1
            EventType.DROPPEDTHIRD -> box.strikeOut += 1
            EventType.STRIKEOUT -> box.strikeOut += 1
            EventType.WALK -> box.baseOnBalls += 1
            EventType.STEALBASE -> box.stealBase += 1
            else -> null
        }
    }


    for (event in this) {
        // 上半局 打者event加進去guest那邊
        if (event.inning % 2 == 1) {
            var noHitter = true
            for (oneHitterBox in guestResult) {
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId)
                updateBox(event, newHitterBox)
                guestResult.add(newHitterBox)
            }
        } else {
            var noHitter = true
            for (oneHitterBox in homeResult) {
                // 找到我要的欄位 已經有這個球員了更新
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId)
                updateBox(event, newHitterBox)
                homeResult.add(newHitterBox)
                Log.i("gillian", "home result加東西")
            }
        }
    }

    //guestResult.removeAt(0)
    //homeResult.removeAt(0)

    //TODO()
    Log.i("gillian", "give me $guestResult and it size ${guestResult.size}" )
    Log.i("gillian", "also home $homeResult and it size ${homeResult.size}")
    return homeResult
}


fun List<Event>.toPersonalScore() : List<String> {

    val result = mutableListOf<String>()

    for (event in this) {
        if (event.player.playerId == UserManager.userId) {
            result.add(
                    when (event.result) {
                        EventType.SINGLE.number -> EventType.SINGLE.letter
                        EventType.DOUBLE.number -> EventType.DOUBLE.letter
                        EventType.TRIPLE.number -> EventType.TRIPLE.letter
                        EventType.HOMERUN.number -> EventType.HOMERUN.letter
                        EventType.HITBYPITCH.number -> EventType.HITBYPITCH.letter
                        EventType.ERRORONBASE.number -> EventType.ERRORONBASE.letter
                        EventType.DROPPEDTHIRD.number -> EventType.DROPPEDTHIRD.letter
                        EventType.WALK.number -> EventType.WALK.letter
                        EventType.STRIKEOUT.number -> EventType.STRIKEOUT.letter
                        EventType.FIELDERCHOICE.number -> EventType.FIELDERCHOICE.letter
                        EventType.GROUNDOUT.number -> EventType.GROUNDOUT.letter
                        EventType.AIROUT.number -> EventType.AIROUT.letter
                        EventType.SACRIFICEFLY.number -> EventType.SACRIFICEFLY.letter
                        EventType.SACRIFICEGO.number -> EventType.SACRIFICEGO.letter
                        else -> ""
                    }
            )
        }
    }

    return result
}


fun List<Event>.toGameStat(isHome: Boolean) {


    val guestPitcher = mutableListOf(PitcherBox())
    val homePitcher = mutableListOf(PitcherBox())
    val guestHitter = mutableListOf(HitterBox())
    val homeHitter = mutableListOf(HitterBox())

    fun updateHitterBox(event: Event, box: HitterBox) {
        val type = event.result
        lateinit var targetEventType: EventType

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }

        // TODO() 之後得分的result要改掉，目前傳上去是0
        if (type == 0) targetEventType = EventType.RUN

        if (targetEventType.isAtBat) box.atBat += 1
        box.run += event.run
        box.runsBattedIn += event.rbi

        when (targetEventType) {
            EventType.SINGLE -> box.hit += 1
            EventType.DOUBLE -> box.hit += 1
            EventType.TRIPLE -> box.hit += 1
            EventType.HOMERUN -> box.hit += 1
            EventType.DROPPEDTHIRD -> box.strikeOut += 1
            EventType.STRIKEOUT -> box.strikeOut += 1
            EventType.WALK -> box.baseOnBalls += 1
            EventType.STEALBASE -> box.stealBase += 1
            else -> null
        }
    }

    fun updatePitcherBox(event: Event, box: PitcherBox) {
        val type = event.result
        lateinit var targetEventType: EventType

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }

        box.run += event.run

        when (targetEventType) {
            EventType.SINGLE -> box.hit += 1
            EventType.DOUBLE -> box.hit += 1
            EventType.TRIPLE -> box.hit += 1
            EventType.HOMERUN -> {
                box.hit += 1
                box.homerun += 1
            }
            EventType.RUN -> box.run += 1
            EventType.DROPPEDTHIRD -> box.strikeOut += 1
            EventType.STRIKEOUT -> box.strikeOut += 1
            EventType.WALK -> box.baseOnBalls += 1
            else -> null // 可能要throw error
        }
    }


    for (event in this) {
        // 上半局 打者event加進去guest那邊
        if (event.inning % 2 == 1) {


            var noHitter = true
            var noPitcher = true

            // 感覺更好的做法是同時找到pitcher和hitter的box
            for (oneHitterBox in guestHitter) {
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateHitterBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId)
                updateHitterBox(event, newHitterBox)
                guestHitter.add(newHitterBox)
            }



            for (onePitcherBox in homePitcher) {
                if (onePitcherBox.playerId == event.pitcher.playerId) {
                    updatePitcherBox(event, onePitcherBox)
                    noPitcher = false
                    break
                }
            }

            if (noPitcher) {
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId)
                updatePitcherBox(event, newPitcherBox)
                homePitcher.add(newPitcherBox)
            }




        } else {
            // 下半局

            var noHitter = true
            var noPitcher = true

            // 感覺更好的做法是同時找到pitcher和hitter的box
            for (oneHitterBox in homeHitter) {
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateHitterBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId)
                updateHitterBox(event, newHitterBox)
                homeHitter.add(newHitterBox)
            }



            for (onePitcherBox in guestPitcher) {
                if (onePitcherBox.playerId == event.pitcher.playerId) {
                    updatePitcherBox(event, onePitcherBox)
                    noPitcher = false
                    break
                }
            }

            if (noPitcher) {
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId)
                updatePitcherBox(event, newPitcherBox)
                guestPitcher.add(newPitcherBox)
            }


        }
    }

    //guestResult.removeAt(0)
    //homeResult.removeAt(0)
}