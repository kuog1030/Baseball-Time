package com.gillian.baseball.team.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.PagerTeammateBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.newplayer.NewPlayerDialog
import com.gillian.baseball.team.TeamFragmentDirections
import com.gillian.baseball.team.TeamViewModel

class TeammatePager : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = PagerTeammateBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerTeamTeammate.adapter = TeammateAdapter(TeammateAdapter.OnClickListener{ player ->
            findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToStatPlayerFragment(player.id))
        })

        binding.layoutTeamMyCard.setOnClickListener{
            findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToStatPlayerFragment(UserManager.playerId))
        }

        binding.layoutTeammateSwipe.setOnRefreshListener {
            viewModel.initTeamPage()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.layoutTeammateSwipe.isRefreshing = it
            }
        })


        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                val newPlayerDialog = NewPlayerDialog(true)
                newPlayerDialog.show(childFragmentManager, "")
                viewModel.onNewPlayerDialogShowed()
            }
        })

        return binding.root
    }
}