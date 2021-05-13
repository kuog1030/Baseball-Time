package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.DialogHitterBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.GameAdapter
import com.gillian.baseball.game.batting.BattingViewModel

//class HitterDialog : AppCompatDialogFragment() {
//
//    //val viewModel by viewModels<BattingViewModel> { getVmFactory() }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//
//        val binding = DialogHitterBinding.inflate(inflater, container, false)
//
//        binding.viewpagerHitter.adapter = GameAdapter(childFragmentManager)
//        binding.tabsHitter.setupWithViewPager(binding.viewpagerHitter)
//
//        return binding.root
//    }
//}