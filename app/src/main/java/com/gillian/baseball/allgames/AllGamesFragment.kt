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
import com.gillian.baseball.data.Team
import com.gillian.baseball.databinding.FragmentAllGamesBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.login.UserManager

class AllGamesFragment : Fragment() {

    private val viewModel by viewModels<AllGamesViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAllGamesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val tempTeam = Team(id = UserManager.teamId, name = UserManager.teamName, acronym = UserManager.teamAcronym)

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
                findNavController().navigate(NavigationDirections.navigationToNewGame(tempTeam))
                viewModel.onNewGameCreated()
            }
        })


        return binding.root
    }
}