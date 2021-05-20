package com.gillian.baseball.allgames.scores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.data.Box
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.PagerScoresBinding
import com.gillian.baseball.util.CurrentDayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay

class ScoresPager : Fragment() {

    private lateinit var binding : PagerScoresBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PagerScoresBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireParentFragment()).get(AllGamesViewModel::class.java)
        viewModel


        addDot(2021,5,10)

        val adapter = CardScoreAdapter()
        binding.recyclerScores.adapter = adapter

        val box1 = Box()
        val box2 = Box()
        val box3 = Box()
        box1.run[0] = 5
        box2.run[1] = 10
        box3.run[0] = 7
        box3.run[1] = 7

        adapter.submitList(listOf(Game("01", "全國大專盃4強", box = box1),
                Game(id = "03", title = "全國大專盃8強", box = box2),
                Game(id = "09", title = "全國大專盃16強", box = box3)))


        return binding.root
    }

    private fun addDot(year: Int, month: Int, day: Int) {
        binding.calendarScores.addDecorators(
            CurrentDayDecorator(
                currentDay = CalendarDay.from(year, month, day)
            )
        )
    }

}