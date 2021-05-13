package com.gillian.baseball.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory

class GameFragment : Fragment() {

    // mock data
    val game = Game(name = "AppWorks School企業盃聯賽8強")
    private val viewModel by viewModels<GameViewModel> {getVmFactory(game) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.viewpagerGame.adapter = GameAdapter(childFragmentManager)
        binding.tabsGame.setupWithViewPager(binding.viewpagerGame)



        return binding.root
    }
}