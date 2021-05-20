package com.gillian.baseball.allgames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentAllGamesBinding
import com.gillian.baseball.ext.getVmFactory

class AllGamesFragment : Fragment() {

    private val viewModel by viewModels<AllGamesViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAllGamesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel

        binding.viewpagerAllGames.adapter = AllGamesAdapter(childFragmentManager)
        binding.tabsAllGames.setupWithViewPager(binding.viewpagerAllGames)

        viewModel.navigateToOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigationToOrder())
                viewModel.onNewGameStarted()
            }
        })

        viewModel.navigateToNewGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigationToNewGame(viewModel.myTeam.value))
                viewModel.onNewGameCreated()
            }
        })


        return binding.root
    }
}