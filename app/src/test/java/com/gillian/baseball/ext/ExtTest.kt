package com.gillian.baseball.ext

import com.gillian.baseball.data.EventPlayer
import org.junit.Assert.*
import org.junit.Test

class ExtTest {

    @Test
    fun getNextLineUpPlayer_beginOfSecondRound_returnFirstPlayer() {
        val lineUp = mutableListOf<EventPlayer>(
                EventPlayer(name = "Aaron"),
                EventPlayer(name = "Ben"),
                EventPlayer(name = "Chloe")
        )
        val result = lineUp.lineUpPlayer(3)

        assertEquals("Aaron", result.name)
    }

    @Test
    fun getBaseballInningCount_tripleOut_returnInteger() {
        val totalOut = 3
        val result = totalOut.toInningCount()

        assertEquals("1.0", result)
    }

    @Test
    fun getBaseballInningCount_oneExtraOut_returnPointOne() {
        val totalOut = 4
        val result = totalOut.toInningCount()

        assertEquals("1.1", result)
    }

    @Test
    fun getBaseballInningCount_twoExtraOut_returnPointTwo() {
        val totalOut = 8
        val result = totalOut.toInningCount()

        assertEquals("2.2", result)

    }

    @Test
    fun getBaseballInningCount_lessThanThree_returnZeroPointOne() {
        val totalOut = 1
        val result = totalOut.toInningCount()

        assertEquals("0.1", result)

    }

    @Test
    fun getBaseballInningCount_zeroOut_returnZeroPointZero() {
        val totalOut = 0
        val result = totalOut.toInningCount()

        assertEquals("0.0", result)

    }

}