package com.gillian.baseball.ext

import android.util.Log
import com.gillian.baseball.data.*
import com.gillian.baseball.game.EventType
import com.gillian.baseball.login.UserManager

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

fun Int.toInningCount() : String {
    return ("${this/3}.${this%3}")
}

// TODO()目前只改to my game stat並且有修改過東西 之後要把to both game stat也更新
fun List<Event>.toBothGameStat() : Statistic {


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

        // TODO() 之後得分的result要改掉，目前傳上去是0
        if (type == 0) targetEventType = EventType.SACRIFICEFLY

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
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId, order = event.player.order)
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
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId, order = event.player.order)
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
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId, order = event.player.order)
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
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId, order = event.player.order)
                updatePitcherBox(event, newPitcherBox)
                guestPitcher.add(newPitcherBox)
            }


        }
    }

    //guestResult.removeAt(0)
    //homeResult.removeAt(0)

    return Statistic(guestPitcher = guestPitcher,
            homePitcher = homePitcher,
            guestHitter = guestHitter,
            homeHitter = homeHitter)
}

fun List<Event>.toMyGameStat(isHome: Boolean) : MyStatistic {


    val myPitcher = mutableListOf(PitcherBox())
    val myHitter = mutableListOf(HitterBox())
    val myScore = mutableListOf<PersonalScore>()

    fun updateHitterBox(event: Event, box: HitterBox) {
        val type = event.result
        lateinit var targetEventType: EventType

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }

        if (type == 0) {
            targetEventType = EventType.RUN
            Log.i("gillian extension", "type = 0 and it shouldn't be print.")
        }

        if (targetEventType.isAtBat) box.atBat += 1
        box.run += event.run
        box.runsBattedIn += event.rbi

        when (targetEventType) {
            EventType.SINGLE -> {
                box.hit += 1
                box.single += 1
            }
            EventType.DOUBLE -> {
                box.hit += 1
                box.douBle += 1
            }
            EventType.TRIPLE -> {
                box.hit += 1
                box.triple += 1
            }
            EventType.HOMERUN -> {
                box.hit += 1
                box.homerun += 1
            }
            EventType.SACRIFICEFLY -> box.sacrificeFly += 1
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

        Log.i("gillian", "update pitcher box event = $event")

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }


        if (type == 0) {
            targetEventType = EventType.SACRIFICEFLY
            Log.i("gillian", "event result is 0. this is not suppose to be printed")
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
            //EventType.RUN -> box.run += 1 上面加過了這邊應該不用加
            EventType.DROPPEDTHIRD -> box.strikeOut += 1
            EventType.STRIKEOUT -> box.strikeOut += 1
            EventType.WALK -> box.baseOnBalls += 1
            // inning pitched is put into out while recording event
            EventType.INNINGSPITCHED -> box.inningsPitched = event.out
            else -> null // TODO() 可能要throw error
        }
    }

    fun updateMyPerformance(event: Event) {
        for (type in EventType.values()) {
            if ((event.result == type.number) && type.isBatting) {
                myScore.add( PersonalScore(
                    type = type.letter,
                    time = event.time,
                    color = type.color
                ) )
                break
            }
        }
    }


    for (event in this) {
        // home team offense (hitting) during bottom inning (for example 3 bottom),
        // which total inning is even ( 3 bottom = 6 )
        // home hitting -> false xor true -> true
        if ((event.inning % 2 == 1) xor (isHome)) {

            Log.i("gillian", "inning ${event.inning} and is Home 紀錄hitter")
            var noHitter = true

            for (oneHitterBox in myHitter) {
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateHitterBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId, order = event.player.order)
                updateHitterBox(event, newHitterBox)
                myHitter.add(newHitterBox)
            }

            if (event.player.playerId == UserManager.playerId) {
                updateMyPerformance(event)
            }

        } else {
            Log.i("gillian", "inning ${event.inning} and is Home 紀錄pitcher")
            var noPitcher = true
            for (onePitcherBox in myPitcher) {
                if (onePitcherBox.playerId == event.pitcher.playerId) {
                    updatePitcherBox(event, onePitcherBox)
                    noPitcher = false
                    break
                }
            }

            if (noPitcher) {
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId, order = event.player.order)
                updatePitcherBox(event, newPitcherBox)
                myPitcher.add(newPitcherBox)
            }

        }
    }

    myPitcher.sortBy {it.order}
    myHitter.sortBy {it.order}
    myScore.sortBy {it.time}

    return MyStatistic(myPitcher = myPitcher, myHitter = myHitter, myPersonalScore = myScore)
}