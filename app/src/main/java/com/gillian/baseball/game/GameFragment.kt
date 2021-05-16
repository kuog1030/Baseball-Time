package com.gillian.baseball.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.MainActivity
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory

class GameFragment : Fragment() {

    // mock data
//    val args: GameFragmentArgs by navArgs()
    // 下面這是能跑的
    private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }
    //val viewModel by viewModels<GameViewModel> ({activity as MainActivity}) {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame)}
    //val viewModel: GameViewModel by lazy {ViewModelProvider(this).get(GameViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        Log.i("gillian", "in game fragment $viewModel")
//        binding.viewpagerGame.adapter = GameAdapter(childFragmentManager)
//        binding.tabsGame.setupWithViewPager(binding.viewpagerGame)



        return binding.root
    }
}