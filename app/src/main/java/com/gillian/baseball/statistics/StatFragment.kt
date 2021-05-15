package com.gillian.baseball.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gillian.baseball.databinding.FragmentStatBinding

class StatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatBinding.inflate(inflater, container, false)

        return binding.root
    }
}