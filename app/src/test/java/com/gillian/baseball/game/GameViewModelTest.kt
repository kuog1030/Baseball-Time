package com.gillian.baseball.game

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.data.Game
import com.gillian.baseball.data.GameTeam
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.source.FakeTestRepository
import com.gillian.baseball.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {

    private lateinit var repository: FakeTestRepository

    private lateinit var viewModel: GameViewModel

    private lateinit var myGame : MyGame

    private lateinit var fakeLineUp : MutableList<EventPlayer>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        fakeLineUp = mutableListOf(EventPlayer(name = "Aaron"), EventPlayer(name = "Ben"))
        myGame = MyGame(true, mutableListOf(), Game(home = GameTeam(lineUp = fakeLineUp), guest = GameTeam(lineUp = fakeLineUp)))
        repository = FakeTestRepository()
        viewModel = GameViewModel(repository, myGame)
    }

    @Test
    fun addFoulBall_startWithZero_returnOne() {

        viewModel.foul()

        val value = viewModel.strikeCount.getOrAwaitValue()

        assertEquals(1, value)

    }

    @Test
    fun addFoulBall_fourTimes_returnTwo() {
        viewModel.foul()
        viewModel.foul()
        viewModel.foul()
        viewModel.foul()

        val value = viewModel.strikeCount.getOrAwaitValue()

        assertEquals(2, value)
    }

    @Test
    fun testAdvanceBase_fullBase_returnFullBase() {
        val originOnBase : Array<EventPlayer?> = arrayOf(EventPlayer(name = "Aaron"),
                EventPlayer(name = "Ben"),
                EventPlayer(name = "Chloe"),
                EventPlayer(name = "David"))

        viewModel.baseList = originOnBase

        val hasHbi = viewModel.advanceBase(0)

        assertEquals(null, viewModel.baseList[0])
        assertEquals("Aaron", viewModel.baseList[1]?.name)
        assertEquals("Ben", viewModel.baseList[2]?.name)
        assertEquals("Chloe", viewModel.baseList[3]?.name)
        assertEquals(true, hasHbi)
    }

    @Test
    fun testAdvanceBase_baseOne_returnOneTwo() {
        val originOnBase : Array<EventPlayer?> = arrayOf(EventPlayer(name = "Aaron"),
                EventPlayer(name = "Ben"),
                null,
                null)

        viewModel.baseList = originOnBase

        val hasHbi = viewModel.advanceBase(0)

        assertEquals(null, viewModel.baseList[0])
        assertEquals("Aaron", viewModel.baseList[1]?.name)
        assertEquals("Ben", viewModel.baseList[2]?.name)
        assertEquals(null, viewModel.baseList[3]?.name)
        assertEquals(false, hasHbi)
    }

    @Test
    fun testAdvanceBase_baseOneThird_returnFull() {
        val originOnBase : Array<EventPlayer?> = arrayOf(EventPlayer(name = "Aaron"),
                EventPlayer(name = "Ben"),
                null,
                EventPlayer(name = "David"))

        viewModel.baseList = originOnBase

        val hasHbi = viewModel.advanceBase(0)

        assertEquals(null, viewModel.baseList[0])
        assertEquals("Aaron", viewModel.baseList[1]?.name)
        assertEquals("Ben", viewModel.baseList[2]?.name)
        assertEquals("David", viewModel.baseList[3]?.name)
        assertEquals(false, hasHbi)
    }

    @Test
    fun testAdvanceBase_baseEmpty_returnOne() {
        val originOnBase : Array<EventPlayer?> = arrayOf(EventPlayer(name = "Aaron"),
                null,
                null,
                null)

        viewModel.baseList = originOnBase

        val hasHbi = viewModel.advanceBase(0)

        assertEquals(null, viewModel.baseList[0])
        assertEquals("Aaron", viewModel.baseList[1]?.name)
        assertEquals(null, viewModel.baseList[2]?.name)
        assertEquals(null, viewModel.baseList[3]?.name)
        assertEquals(false, hasHbi)
    }

    @Test
    fun testAdvanceBase_baseTwoThird_returnFull() {
        val originOnBase : Array<EventPlayer?> = arrayOf(EventPlayer(name = "Aaron"),
                null,
                EventPlayer(name = "Chloe"),
                EventPlayer(name = "David"))

        viewModel.baseList = originOnBase

        val hasHbi = viewModel.advanceBase(0)

        assertEquals(null, viewModel.baseList[0])
        assertEquals("Aaron", viewModel.baseList[1]?.name)
        assertEquals("Chloe", viewModel.baseList[2]?.name)
        assertEquals("David", viewModel.baseList[3]?.name)
        assertEquals(false, hasHbi)
    }


//    @Test
//    fun testNextAtNumber_lastPlayer_returnZero() {
//        val currentNum = 2
//        val lineUpSize = 3
//        val result = viewModel.nextAtBatNumber(currentNum, lineUpSize)
//
//        assertEquals(0, result)
//    }
//
//    @Test
//    fun testNextAtNumber_startFromMiddle_returnNext() {
//        val currentNum = 2
//        val lineUpSize = 9
//        val result = viewModel.nextAtBatNumber(currentNum, lineUpSize)
//
//        assertEquals(3, result)
//    }



}