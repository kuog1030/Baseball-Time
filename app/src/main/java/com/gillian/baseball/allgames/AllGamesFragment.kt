package com.gillian.baseball.allgames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.data.Team
import com.gillian.baseball.databinding.FragmentAllGamesBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.login.UserManager

class AllGamesFragment : Fragment() {

    private val args: AllGamesFragmentArgs by navArgs()
    private val viewModel by viewModels<AllGamesViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAllGamesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner



        binding.viewpagerAllGames.adapter = AllGamesAdapter(childFragmentManager)
        binding.tabsAllGames.setupWithViewPager(binding.viewpagerAllGames)

        if (args.needRefresh) viewModel.refresh()

        // fast start a new game
        viewModel.startNewGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigationToOrder(null))
                viewModel.onNewGameStarted()
            }
        })

        viewModel.navigateToNewGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AllGamesFragmentDirections.actionAllGamesToNewGame(UserManager.team ?: Team()))
                viewModel.onNewGameCreated()
            }
        })

        viewModel.allGameCards.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.seperateFinishedGame()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i("gillian", "on resume")
    }

    override fun onStart() {
        super.onStart()
        Log.i("gillian", "on start")
    }
}