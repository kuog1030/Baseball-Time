package com.gillian.baseball.statistics.statteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.FragmentStatTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

class StatTeamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatTeamBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.teamPlayers.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.createStatTable(it)
            }
        })

        binding.recyclerTeamStatHitter.adapter = HitterBoxAdapter()
        binding.recyclerTeamStatPitcher.adapter = PitcherBoxAdapter()


        return binding.root
    }
}