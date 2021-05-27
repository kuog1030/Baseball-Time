package com.gillian.baseball.statistics.statplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentStatPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

class StatPlayerFragment : Fragment() {

    private val viewModel by viewModels<StatPlayerViewModel> { getVmFactory() }
    private val args: StatPlayerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentStatPlayerBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getPlayerStat(args.playerId)

        viewModel.player.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.hitStat = it.hitStat
                binding.pitchStat = it.pitchStat
                viewModel.updateMoreHitStat()
            }
        })


        return binding.root
    }

}