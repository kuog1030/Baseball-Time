package com.gillian.baseball.allgames.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.databinding.PagerScheduleBinding

class SchedulePager : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerScheduleBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireParentFragment()).get(AllGamesViewModel::class.java)
        binding.viewModel = viewModel


        return binding.root
    }

}