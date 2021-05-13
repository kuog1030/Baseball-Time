package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.data.Event
import com.gillian.baseball.databinding.DialogEventBinding
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.game.batting.BattingViewModel

class EventDialog(val event : Event)  : AppCompatDialogFragment() {

    //val args : EventDialogArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //val event = args.preEvent
        val binding = DialogEventBinding.inflate(inflater, container, false)
        //val viewModel = ViewModelProvider(requireActivity()).get(EventDialogViewModel::class.java)
        val viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory((requireContext().applicationContext as BaseballApplication).repository)
        ).get(EventDialogViewModel::class.java)



        viewModel.event.value = event

        fun changePage(){
            val currentPosition = binding.viewpagerEvent.currentItem
            if ( currentPosition >= 0 ) {
                binding.viewpagerEvent.setCurrentItem( currentPosition + 1 , true)
            }
        }

        binding.viewpagerEvent.adapter = EventDialogAdapter(childFragmentManager)
        binding.tabsEvent.setupWithViewPager(binding.viewpagerEvent)

        binding.buttonEventForward.setOnClickListener{
            changePage()
        }

        viewModel.changeToNextPage.observe(viewLifecycleOwner, Observer {
            it?.let{
                changePage()
                viewModel.onNextPageChanged()
            }
        })

        viewModel.dismiss.observe(viewLifecycleOwner, Observer {
            it?.let{
                dismiss()
                val battingViewModel = ViewModelProvider(requireParentFragment()).get(BattingViewModel::class.java)
                battingViewModel.nextPlayer()
                Log.i("gillian", "in event dialog $battingViewModel")
                //val battingViewModel by viewModels<BattingViewModel> { getVmFactory() }
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }
}