package com.gillian.baseball.allgames.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.R
import com.gillian.baseball.allgames.AllGamesFragmentDirections
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.allgames.CardScoreAdapter
import com.gillian.baseball.data.Team
import com.gillian.baseball.databinding.PagerScheduleBinding
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util

class SchedulePager : Fragment() {

    lateinit var binding: PagerScheduleBinding
    lateinit var viewModel: AllGamesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PagerScheduleBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireParentFragment()).get(AllGamesViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerSchedule.adapter = CardScoreAdapter(CardScoreAdapter.OnClickListener {
            findNavController().navigate(NavigationDirections.navigationToOrder(it))
        }, false, viewModel)

        binding.layoutScheduleSwipe.setOnRefreshListener {
            viewModel.refresh()
        }

        val rotateOpen = Util.getAnim(R.anim.rotate_open_anim)
        val rotateClose = Util.getAnim(R.anim.rotate_close_anim)
        val fromBottom = Util.getAnim(R.anim.from_bottom_anim)
        val toBottom = Util.getAnim(R.anim.to_bottom_anim)

        // no fab animation needed if the fragment is first-time-created
        var firstEnter = true

        // fast start a new game
        viewModel.startNewGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigationToOrder(null))
                viewModel.onNewGameStarted()
            }
        })

        viewModel.navigateToNewGame.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AllGamesFragmentDirections.actionAllGamesToNewGame(UserManager.team ?: Team()))
                viewModel.onNewGameCreated()
            }
        })

        viewModel.clicked.observe(viewLifecycleOwner, Observer {
            it?.let { fabClicked ->
                if (fabClicked) {
                    startFabAnimation(fromBottom, rotateOpen)
                } else {
                    if (firstEnter) {
                        firstEnter = !firstEnter
                    } else {
                        startFabAnimation(toBottom, rotateClose)
                        binding.fabScheduleGame.clearAnimation()
                        binding.fabFastStart.clearAnimation()
                    }
                }
            }
        })

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutScheduleSwipe.isRefreshing = it
            }
        })

        return binding.root
    }


    private fun startFabAnimation(showAnim: Animation, rotateAnim: Animation) {
        binding.fabAdd.startAnimation(rotateAnim)
        binding.fabScheduleGame.startAnimation(showAnim)
        binding.fabFastStart.startAnimation(showAnim)
    }


    override fun onStop() {
        super.onStop()
        viewModel.clicked.value = false
    }

}