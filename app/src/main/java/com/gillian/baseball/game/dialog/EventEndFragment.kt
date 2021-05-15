package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.FragmentEventEndBinding

class EventEndFragment(val page: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventEndBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(EventDialogViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.page = page
        binding.viewModel = viewModel


        return binding.root
    }
}