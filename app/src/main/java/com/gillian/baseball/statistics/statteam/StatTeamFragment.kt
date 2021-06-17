package com.gillian.baseball.statistics.statteam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.databinding.FragmentStatTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

class StatTeamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatTeamBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerTeamStatHitter.adapter = HitterBoxAdapter()
        binding.recyclerTeamStatPitcher.adapter = PitcherBoxAdapter()

        binding.buttonTeamStatLeave.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}