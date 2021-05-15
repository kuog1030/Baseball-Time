package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.databinding.DialogEventBinding
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.game.batting.BattingViewModel

class EventDialog(val argsAtBase: List<AtBase>, val isSafe: Boolean, val argsHitterEvent: Event) : AppCompatDialogFragment() {

//    想要設定dialog的寬度的話，新增下面這段，並進到dialog_event.xml裡面調整長寬和背景顏色
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog)
//    }
//    binding.layoutEventOutside.setOnClickListener{
//        dismiss()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

//        for (i in argsAtBase) {
//            Log.i("at base debuging", "event dialog $i")
//        }

        val binding = DialogEventBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory((requireContext().applicationContext as BaseballApplication).repository)
        ).get(EventDialogViewModel::class.java)


        viewModel.atBaseList = argsAtBase
        viewModel.hitterEvent.value = argsHitterEvent

        fun changePage() {
            val currentPosition = binding.viewpagerEvent.currentItem
            if (currentPosition >= 0) {
                binding.viewpagerEvent.setCurrentItem(currentPosition + 1, true)
            }
        }

        binding.viewpagerEvent.adapter = EventDialogAdapter(childFragmentManager, isSafe, argsAtBase)
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
                val battingViewModel = ViewModelProvider(requireParentFragment()).get(BattingViewModel::class.java)
                if (viewModel.hasOut != null) {
                    battingViewModel.setNewBaseList(it)
                    if (viewModel.hasBaseOut != null) battingViewModel.onBaseOut(viewModel.hasBaseOut!!)
                    battingViewModel.out()
                } else {
                    battingViewModel.setNewBaseList(it)
                    battingViewModel.nextPlayer()
                }
                Log.i("gillian", "in event dialog $battingViewModel")
                //val battingViewModel by viewModels<BattingViewModel> { getVmFactory() }
                dismiss()
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }
}