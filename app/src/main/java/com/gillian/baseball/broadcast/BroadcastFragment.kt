package com.gillian.baseball.broadcast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.R
import com.gillian.baseball.databinding.FragmentBroadcastBinding
import com.gillian.baseball.ext.getVmFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BroadcastFragment : Fragment() {

    private val args: BroadcastFragmentArgs by navArgs()
    private val viewModel by viewModels<BroadcastViewModel> { getVmFactory(args.game) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBroadcastBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = BroadcastAdapter()
        binding.recyclerLive.adapter = adapter


        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.stopBroadcast.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().popBackStack()
                viewModel.onBroadcastOff()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let{
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG ).show()
            }
        })

        binding.buttonLiveClose.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.CustomAlertDialog)
                    .setTitle(getString(R.string.turn_off_broadcast))
                    .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        viewModel.stopBroadcast()
                    }
                    .setNeutralButton(getString(R.string.cancel), null)
                    .show()
        }

        return binding.root
    }
}