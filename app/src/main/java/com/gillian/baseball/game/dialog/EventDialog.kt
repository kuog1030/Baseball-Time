package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.gillian.baseball.databinding.DialogEventBinding

class EventDialog  : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = DialogEventBinding.inflate(inflater, container, false)

        binding.viewpagerEvent.adapter = EventDialogAdapter(childFragmentManager)
        binding.tabsEvent.setupWithViewPager(binding.viewpagerEvent)

        return binding.root
    }
}