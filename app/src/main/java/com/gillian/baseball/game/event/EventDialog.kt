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
//        val viewModel = ViewModelProvider(
//                requireActivity(),
//                ViewModelFactory((requireContext().applicationContext as BaseballApplication).repository)
//        ).get(EventDialogViewModel::class.java)


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

        viewModel.dismiss.observe(viewLifecycleOwner, Observer {
            it?.let {
                val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)
                gameViewModel.addPitchCount()
                if (viewModel.hasOut != null) {
                    // 如果有出局數的話 eg. 雖然上壘safe 但是選殺，有仍然有出局數

                    // 如果有壘包上的出局數 // 要先處理不然baselist被清空了 例如雙殺打就不會記錄到
                    if (!viewModel.hasBaseOut.isEmpty()) gameViewModel.onBaseOut(viewModel.hasBaseOut, EventType.ONBASEOUT)


                    gameViewModel.setNewBaseList(it)
                    // 犧牲打也要加上分數
                    if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)

                    gameViewModel.out()
                } else {
                    // 如果沒有出局數的話 is safe = true
                    // 如果有得分，更新畫面得分數&更新box
                    if (viewModel.scoreToBeAdded != 0) gameViewModel.scored(viewModel.scoreToBeAdded)
                    gameViewModel.setNewBaseList(it)
                    gameViewModel.nextPlayer()
                }

                // 更新安打的box
                if (viewModel.hitToBeAdded != 0) gameViewModel.addHitToBox(viewModel.hitToBeAdded)
                dismiss()
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }
}