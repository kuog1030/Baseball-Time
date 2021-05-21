package com.gillian.baseball.statistics.statgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gillian.baseball.databinding.FragmentStatGameBinding

class StatGameFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatGameBinding.inflate(inflater, container, false)

        return binding.root
    }
}