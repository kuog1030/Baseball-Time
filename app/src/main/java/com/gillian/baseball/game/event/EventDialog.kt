package com.gillian.baseball.game.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.R
import com.gillian.baseball.data.EventInfo
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.databinding.DialogEventBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.EventType
import com.gillian.baseball.game.GameViewModel

class EventDialog(private val eventInfo: EventInfo) : AppCompatDialogFragment() {

    private val isSafe = eventInfo.isSafe
    private val atBaseList = eventInfo.atBaseList
    private val viewModel by viewModels<EventViewModel> { getVmFactory(eventInfo) }
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GameDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = DialogEventBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)


        fun changePage() {
            val currentPosition = binding.viewpagerEvent.currentItem
            if (currentPosition >= 0) {
                binding.viewpagerEvent.setCurrentItem(currentPosition + 1, true)
            }
        }

        fun changeToEnd() {
            binding.viewpagerEvent.setCurrentItem(atBaseList.size, true)
        }

        binding.viewpagerEvent.adapter = EventDialogAdapter(childFragmentManager, isSafe, atBaseList)
        binding.tabsEvent.setupWithViewPager(binding.viewpagerEvent)
        binding.viewpagerEventArrow.setArrowIndicatorRes(R.drawable.ic_arrow_backward_24, R.drawable.ic_arrow_forward_24)
        binding.viewpagerEventArrow.bind(binding.viewpagerEvent)

        // change page after button clicked
        viewModel.changeToNextPage.observe(viewLifecycleOwner, Observer {
            it?.let { toTheEnd ->
                if (toTheEnd) {
                    changeToEnd()
                } else {
                    changePage()
                }
                viewModel.onNextPageChanged()
            }
        })

        binding.buttonEventDismiss.setOnClickListener {
            dismiss()
        }

        viewModel.dismiss.observe(viewLifecycleOwner, Observer {
            it?.let {
                gameViewModel.addPitchCount()
                updatePlayerResult(it, hitterOut = !isSafe, runnerOut = viewModel.hasBaseOut.isNotEmpty())
                // update hit box
                if (viewModel.hitToBeAdded != 0) gameViewModel.addHitToBox(viewModel.hitToBeAdded)
                // update error box if error occurred
                if (viewModel.atBaseList[0].eventType == EventType.ERRORONBASE) gameViewModel.addErrorToBox(1)
                dismiss()
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }


    private fun updatePlayerResult(newBaseList: Array<EventPlayer?>, hitterOut: Boolean, runnerOut: Boolean) {
        if (runnerOut) {
            gameViewModel.setNewBaseList(newBaseList)
            if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)
            if (hitterOut) {
                // double play, triple play
                gameViewModel.out(viewModel.hasBaseOut.size + 1)
            } else {
                // fielder choice -> runner out but hitter safe
                gameViewModel.out(1)
            }
        } else {
            if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)
            gameViewModel.setNewBaseList(newBaseList)
            if (hitterOut) {
                // for example sacrifice fly
                gameViewModel.out(1)
            } else {
                gameViewModel.nextPlayer()
            }
        }
    }
}