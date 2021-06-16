package com.gillian.baseball.allgames.scores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.databinding.PagerScoresBinding

class ScoresPager : Fragment() {

    private lateinit var binding: PagerScoresBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PagerScoresBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel = ViewModelProvider(requireParentFragment()).get(AllGamesViewModel::class.java)
        

        binding.recyclerScores.adapter = CardScoreAdapter(CardScoreAdapter.OnClickListener{ game ->
            //navigation
            //TODO() wayne的做法是call viewmodel 的navigate function 然後在同樣這個fragment去observe他
            findNavController().navigate(NavigationDirections.navigationToStatGame(game.id, game.isHome))
        })
        binding.viewModel = viewModel


        return binding.root
    }

}