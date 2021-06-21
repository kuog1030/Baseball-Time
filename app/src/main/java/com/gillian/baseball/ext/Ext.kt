package com.gillian.baseball.ext

import android.util.Log
import com.gillian.baseball.R
import com.gillian.baseball.data.*
import com.gillian.baseball.game.EventType
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util.getString

fun MutableList<EventPlayer>.lineUpPlayer(number: Int): EventPlayer {
    return (this[(number % this.size)])
}

fun Int.toInningCount(): String {
    return ("${this / 3}.${this % 3}")
}

fun List<Event>.toMyGameStat(isHome: Boolean): MyStatistic {
    val myPitcher = mutableListOf(PitcherBox(order = -2))
    val myHitter = mutableListOf(HitterBox())
    val myScore = mutableListOf<PersonalScore>()

    val errorEvent = mutableListOf<Event>()

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
            Log.i("gillian", "event result is 0. this is not suppose to occur")
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
            EventType.HITBYPITCH -> box.hitByPitch += 1
            EventType.SACRIFICEHIT -> box.sacrificeHit += 1
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

        for (eventType in EventType.values()) {
            if (type == eventType.number) {
                targetEventType = eventType
                break
            }
        }

        if (type == 0) {
            targetEventType = EventType.SACRIFICEFLY
            Log.i("gillian", "event result is 0. this is not suppose to occur")
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
            EventType.DROPPEDTHIRD -> box.strikeOut += 1
            EventType.STRIKEOUT -> box.strikeOut += 1
            EventType.WALK -> box.baseOnBalls += 1
            // inning pitched is put into out while recording event
            EventType.INNINGSPITCHED -> box.inningsPitched = event.out
            EventType.IPEND -> box.inningsPitched = event.out
            EventType.IPUPDATE -> box.earnedRuns = event.out
            else -> null
        }
    }

    fun updateMyPerformance(event: Event) {
        for (type in EventType.values()) {
            if ((event.result == type.number) && type.isBatting) {
                myScore.add(PersonalScore(
                        type = type.letter,
                        time = event.time,
                        color = type.color
                ))
                break
            }
        }
    }


    for (event in this) {
        if (event.result == EventType.INNINGCHANGE.number) {
            continue
        }

        // home team offense (hitting) during bottom inning (for example 3 bottom),
        // which total inning is even ( 3 bottom = 6 )
        // home hitting -> false xor true -> true

        if ((event.inning % 2 == 1) xor (isHome)) {

            var noHitter = true

            // if the player exists in hitter box list, update
            for (oneHitterBox in myHitter) {
                if (oneHitterBox.playerId == event.player.playerId) {
                    updateHitterBox(event, oneHitterBox)
                    noHitter = false
                    break
                }
            }

            // if the player hasn't be recorded, create one, update then add
            if (noHitter) {
                val newHitterBox = HitterBox(name = event.player.name, playerId = event.player.playerId, order = event.player.order)
                updateHitterBox(event, newHitterBox)
                myHitter.add(newHitterBox)
            }

            // if this player is user itself, record personal performance
            if (event.player.playerId == UserManager.playerId) {
                updateMyPerformance(event)
            }

        } else {

            if (event.result == EventType.ERROR.number) {
                errorEvent.add(event)
                continue
            }

            var noPitcher = true
            // if the player exists in pitcher box list, update
            for (onePitcherBox in myPitcher) {
                if (onePitcherBox.playerId == event.pitcher.playerId) {
                    updatePitcherBox(event, onePitcherBox)
                    noPitcher = false
                    break
                }
            }

            // if the player hasn't be recorded, create one, update then add
            if (noPitcher) {
                val newPitcherBox = PitcherBox(name = event.pitcher.name, playerId = event.pitcher.playerId, order = event.pitcher.order)
                updatePitcherBox(event, newPitcherBox)
                myPitcher.add(newPitcherBox)
            }

        }
    }

    for (event in errorEvent) {
        for (oneHitterBox in myHitter) {
            if (oneHitterBox.playerId == event.player.playerId) {
                oneHitterBox.error += 1
                break
            }
        }

        for (onePitcherBox in myPitcher) {
            if (onePitcherBox.playerId == event.player.playerId) {
                onePitcherBox.error += 1
                break
            }
        }
    }

    myPitcher.sortBy { it.order }
    myHitter.sortBy { it.order }
    myScore.sortBy { it.time }

    return MyStatistic(myPitcher = myPitcher, myHitter = myHitter, myPersonalScore = myScore)
}

fun List<Player>.toRankList(): List<Rank> {

    lateinit var sortList: List<Player>
    val resultList = mutableListOf<Rank>()

    fun getResultOfHitStat(hitStat: HitterBox, type: Int): Float {
        return when (type) {
            1 -> hitStat.avg
            2 -> hitStat.homerun.toFloat()
            3 -> hitStat.hit.toFloat()
            else -> hitStat.strikeOut.toFloat()
        }
    }

    fun getFormatOfHitStat(type: Int): String {
        return when (type) {
            1 -> "%.3f"
            else -> "%.0f"
        }
    }

    fun getTitleOfHitStat(type: Int): String {
        return when (type) {
            1 -> getString(R.string.avg_rank)
            2 -> getString(R.string.homerun_rank)
            3 -> getString(R.string.hit_rank)
            else -> getString(R.string.so_rank)
        }
    }

    for (type in 1..4) {
        val result = Rank()
        if (this.size <= 3) {
            result.type = getTitleOfHitStat(type)
            resultList.add(result)
        } else {
            sortList = this.sortedWith(compareBy({ -(getResultOfHitStat(it.hitStat, type)) }, { it.hitStat.atBat }))
            result.let {
                val statFormat = getFormatOfHitStat(type)
                result.type = getTitleOfHitStat(type)
                it.topImage = sortList[0].image ?: ""
                it.topScore = statFormat.format(getResultOfHitStat(sortList[0].hitStat, type))
                it.secondScore = statFormat.format(getResultOfHitStat(sortList[1].hitStat, type))
                it.thirdScore = statFormat.format(getResultOfHitStat(sortList[2].hitStat, type))
            }
            result.topName = sortList[0].name
            result.secondName = sortList[1].name
            result.thirdName = sortList[2].name

            resultList.add(result)
        }
    }
    return resultList
}