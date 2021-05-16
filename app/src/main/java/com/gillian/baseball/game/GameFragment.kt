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
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.MainActivity
import com.gillian.baseball.R
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.factory.GameViewModelFactory
import com.gillian.baseball.game.batting.BattingFragment
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {

    // mock data
//    val args: GameFragmentArgs by navArgs()
    // 下面這是能跑的
   private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }
    //val viewModel by viewModels<GameViewModel> ({activity as MainActivity}) {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame)}

    //沒有用factory的作法
    //val viewModel: GameViewModel by lazy {ViewModelProvider(this).get(GameViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)


//        val viewModelFactory = GameViewModelFactory(
//                (requireContext().applicationContext as BaseballApplication).repository,
//                GameFragmentArgs.fromBundle(requireArguments()).preGame
//        )
        //val viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(GameViewModel::class.java)

        binding.viewModel = viewModel
        Log.i("gillian", "in game fragment $viewModel")


        val battingFragment = BattingFragment()
        childFragmentManager.beginTransaction().replace(R.id.gameNavHostFragment, battingFragment).commit()

//        binding.viewpagerGame.adapter = GameAdapter(childFragmentManager)
//        binding.tabsGame.setupWithViewPager(binding.viewpagerGame)



        return binding.root
    }
}