package com.gillian.baseball.team.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.HitterBox
import com.gillian.baseball.databinding.PagerRankBinding
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.views.HitterBoxAdapter

class RankPager : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerRankBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel = ViewModelProvider(requireParentFragment()).get(TeamViewModel::class.java)

        val adapter = HitterBoxAdapter()
        binding.recyclerHitterBox.adapter = adapter
        adapter.submitList(listOf(HitterBox(playerId = "0", run = 10),
                HitterBox(name = "Gillian", playerId = "155", run = 10),
                HitterBox(name = "Peter", playerId = "100", run = 5),
                HitterBox(name = "Sean", playerId = "134", run = 2),
                HitterBox(name = "Chloe", playerId = "2", run = 1),
                HitterBox(name = "Wency", playerId = "5", run = 7),
                HitterBox(name = "Scolley", playerId = "9", run = 2, hit = 28, strikeOut = 3)
        ))

        return binding.root
    }
}