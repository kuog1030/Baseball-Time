package com.gillian.baseball.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.databinding.ItemOrderPitcherBinding

class PitcherSpinnerAdapter(context: Context, playerList: List<EventPlayer>) : ArrayAdapter<EventPlayer>(context, 0, playerList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }


    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = convertView?.tag as? ItemOrderPitcherBinding
                ?: ItemOrderPitcherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.player = getItem(position)

        return binding.root
    }
}