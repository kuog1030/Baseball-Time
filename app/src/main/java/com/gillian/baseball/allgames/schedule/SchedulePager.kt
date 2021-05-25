package com.gillian.baseball.allgames.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.R
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.databinding.PagerScheduleBinding
import com.gillian.baseball.util.Util

class SchedulePager : Fragment() {

    lateinit var viewModel : AllGamesViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerScheduleBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireParentFragment()).get(AllGamesViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerSchedule.adapter = CardScoreAdapter(CardScoreAdapter.OnClickListener{ it ->
            findNavController().navigate(NavigationDirections.navigationToOrder(it))
        })

        binding.layoutScheduleSwipe.setOnRefreshListener {
            viewModel.refresh()
        }

        val rotateOpen = Util.getAnim(R.anim.rotate_open_anim)
        val rotateClose = Util.getAnim(R.anim.rotate_close_anim)
        val fromBottom = Util.getAnim(R.anim.from_bottom_anim)
        val toBottom = Util.getAnim(R.anim.to_bottom_anim)
        var firstEnter = true

        viewModel.clicked.observe(viewLifecycleOwner, Observer {
            it?.let { clicked ->
                if (clicked) {
                    binding.fabAdd.startAnimation(rotateOpen)
                    binding.fabFastScheduleGame.startAnimation(fromBottom)
                    binding.fabFastStart.startAnimation(fromBottom)
                } else {
                    if (firstEnter) {
                        firstEnter = !firstEnter
                    } else {
                        binding.fabAdd.startAnimation(rotateClose)
                        binding.fabFastScheduleGame.startAnimation(toBottom)
                        binding.fabFastStart.startAnimation(toBottom)
                    }
                }
            }
        })

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.layoutScheduleSwipe.isRefreshing = it
            }
        })


        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.clicked.value = false
    }

}