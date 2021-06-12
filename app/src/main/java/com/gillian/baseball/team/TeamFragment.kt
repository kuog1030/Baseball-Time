package com.gillian.baseball.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.databinding.FragmentTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.newplayer.NewPlayerDialog

class TeamFragment : Fragment() {

   //private val viewModel by viewModels<TeamViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTeamBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewpagerTeams.adapter = TeamAdapter(childFragmentManager)
        binding.tabsTeams.setupWithViewPager(binding.viewpagerTeams)

        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                val newPlayerDialog = NewPlayerDialog(true)
                newPlayerDialog.show(childFragmentManager, "")
                viewModel.onNewPlayerDialogShowed()
            }
        })

        viewModel.initUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                UserManager.team = it
                viewModel.initTeamPage()
            }
        })

        viewModel.teamPlayers.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.createRankList(it)
            }
        })

        return binding.root
    }
}