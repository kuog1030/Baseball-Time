package com.gillian.baseball.statistics.statgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentStatGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.views.HitterBoxAdapter

class StatGameFragment : Fragment() {

    private val viewModel by viewModels<StatGameViewModel> { getVmFactory() }
    private val args: StatGameFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatGameBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerGameStatHitter.adapter = HitterBoxAdapter()

        viewModel.gameId.observe(viewLifecycleOwner, Observer {
            viewModel.getHitterStat()
            Log.i("gillian", "game id change")
        })


        viewModel.gameId.value = args.gameId


        return binding.root
    }
}