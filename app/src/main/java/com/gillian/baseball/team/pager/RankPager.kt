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

        binding.viewModel = viewModel
        binding.recyclerHitterBox.adapter = HitterBoxAdapter()

        return binding.root
    }
}