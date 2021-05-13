package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.DialogEventBinding

class EventDialog  : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = DialogEventBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(EventDialogViewModel::class.java)

        fun changePage(){
            val currentPosition = binding.viewpagerEvent.currentItem
            if ( currentPosition >= 0 ) {
                Log.i("gillian", "it is suppose to be zero $currentPosition")
                binding.viewpagerEvent.setCurrentItem( currentPosition + 1 )
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
                viewModel.onDialogDismiss()
            }
        })

        return binding.root
    }
}