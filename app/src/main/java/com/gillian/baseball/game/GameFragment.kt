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

   private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        //Log.i("gillian", "in game : game View Model $viewModel")


        val battingFragment = BattingFragment(viewModel.game.value!!)
        childFragmentManager.beginTransaction().replace(R.id.gameNavHostFragment, battingFragment).commit()

//        binding.viewpagerGame.adapter = GameAdapter(childFragmentManager)
//        binding.tabsGame.setupWithViewPager(binding.viewpagerGame)



        return binding.root
    }
}