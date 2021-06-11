package com.gillian.baseball.game.event

import android.os.Bundle
import android.util.Log
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
import com.gillian.baseball.databinding.DialogEventBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.EventType
import com.gillian.baseball.game.GameViewModel

class EventDialog(val eventInfo: EventInfo) : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GameDialog)
    }
//    binding.layoutEventOutside.setOnClickListener{
//        dismiss()
//    }

    private val viewModel by viewModels<EventViewModel> { getVmFactory(eventInfo) }
    private val isSafe = eventInfo.isSafe
    private val atBaseList = eventInfo.atBaseList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

//        for (i in argsAtBase) {
//            Log.i("at base debuging", "event dialog $i")
//        }

        val binding = DialogEventBinding.inflate(inflater, container, false)
        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)


        fun changePage() {
            val currentPosition = binding.viewpagerEvent.currentItem
            if (currentPosition >= 0) {
                binding.viewpagerEvent.setCurrentItem(currentPosition + 1, true)
            }
        }

        binding.viewpagerEvent.adapter = EventDialogAdapter(childFragmentManager, isSafe, atBaseList)
        binding.tabsEvent.setupWithViewPager(binding.viewpagerEvent)
        binding.viewpagerEventArrow.setArrowIndicatorRes(R.drawable.ic_baseline_arrow_backward_24, R.drawable.ic_baseline_arrow_forward_24)
        binding.viewpagerEventArrow.bind(binding.viewpagerEvent)

        viewModel.changeToNextPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                changePage()
                viewModel.onNextPageChanged()
            }
        })

        binding.buttonEventDismiss.setOnClickListener{
            dismiss()
        }

        viewModel.dismiss.observe(viewLifecycleOwner, Observer {
            it?.let {

                gameViewModel.addPitchCount()

                if (viewModel.hasBaseOut.isEmpty()) {
                    // 沒有壘上跑者出局
                    // 高飛犧牲打
                    if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)
                    gameViewModel.setNewBaseList(it)
                    Log.i("gillian67", "這邊")
                    if (!isSafe) {
                        gameViewModel.eventOut(1)
                    } else {
                        gameViewModel.nextPlayer()
                    }

                } else {
                    // 有壘上跑者出局
                    gameViewModel.setNewBaseList(it)
                    if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)
                    if (isSafe) {
                        gameViewModel.eventOut(1) // 選殺
                    } else {
                        gameViewModel.eventOut(viewModel.hasBaseOut.size +1) // 雙殺三殺
                    }
                }

                // 更新安打的box
                if (viewModel.hitToBeAdded != 0) gameViewModel.addHitToBox(viewModel.hitToBeAdded)
                if (viewModel.atBaseList[0].eventType == EventType.ERRORONBASE) gameViewModel.addErrorToBox(1)
                dismiss()
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }
}