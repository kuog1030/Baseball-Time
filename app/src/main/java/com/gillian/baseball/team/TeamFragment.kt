package com.gillian.baseball.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.newplayer.NewPlayerDialog

class TeamFragment : Fragment() {

    private val viewModel by viewModels<TeamViewModel> {getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTeamBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewpagerTeams.adapter = TeamAdapter(childFragmentManager)
        binding.tabsTeams.setupWithViewPager(binding.viewpagerTeams)

        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
            it?.let{
                val newPlayerDialog = NewPlayerDialog(true)
                newPlayerDialog.show(childFragmentManager, "")
                viewModel.onNewPlayerDialogShowed()
            }
        })


        return binding.root
    }
}