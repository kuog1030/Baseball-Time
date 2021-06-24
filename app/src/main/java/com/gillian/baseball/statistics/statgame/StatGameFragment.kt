package com.gillian.baseball.statistics.statgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentStatGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.views.BoxAdapter
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PersonalScoreAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

class StatGameFragment : Fragment() {

    private val viewModel by viewModels<StatGameViewModel> { getVmFactory() }
    private val args: StatGameFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatGameBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.fetchMyTeamStat(args.gameId, args.isHome)
        viewModel.fetchGame(args.gameId)

        binding.recyclerGameStatBox.adapter = BoxAdapter()
        binding.recyclerGameStatMy.adapter = PersonalScoreAdapter()
        binding.recyclerGameStatHitter.adapter = HitterBoxAdapter()
        binding.recyclerGameStatPitcher.adapter = PitcherBoxAdapter()

        binding.buttonGameStatLeave.setOnClickListener{
            findNavController().popBackStack()
        }

        return binding.root
    }
}