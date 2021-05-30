package com.gillian.baseball.allbroadcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.databinding.FragmentAllBroadcastBinding
import com.gillian.baseball.ext.getVmFactory

class AllBroadcastFragment : Fragment() {

    private val viewModel by viewModels<AllBroadcastViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAllBroadcastBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        binding.recyclerAllLive.adapter = CardScoreAdapter(CardScoreAdapter.OnClickListener { game ->
            findNavController().navigate(AllBroadcastFragmentDirections.actionAllToOneLive(game.id))
        }, isBroadcast = true)

        return binding.root
    }

}