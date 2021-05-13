package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.FragmentHitterBinding

class HitterFragment(val page : String) : Fragment() {

    //val viewModel by viewModels<HitterViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHitterBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(EventDialogViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.page = page


        return binding.root
    }

}