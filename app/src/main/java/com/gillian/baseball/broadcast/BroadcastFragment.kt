package com.gillian.baseball.broadcast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.databinding.FragmentBroadcastBinding
import com.gillian.baseball.ext.getVmFactory

class BroadcastFragment : Fragment() {

    val args: BroadcastFragmentArgs by navArgs()
    private val viewModel by viewModels<BroadcastViewModel> {getVmFactory(args.game) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBroadcastBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("gillian", "view model liveevent1 ${viewModel.liveEvents}")
        viewModel
        val adapter = BroadcastAdapter()

        binding.recyclerLive.adapter = adapter
        binding.viewModel = viewModel


        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Log.i("gillian", "view model liveevent4 ${viewModel.liveEvents}")
            it?.let{
                Log.i("gillian", "i get new event size = ${it.size} event ${it}")
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.turnOffBroadcast.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().popBackStack()
                viewModel.onBroadcastOff()
            }
        })

        binding.buttonBroadcastClose.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                    .setMessage(getString(R.string.turn_off_broadcast))
                    .setTitle(getString(R.string.turn_off))
                    .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        viewModel.enableBroadcast()
                    }
                    .setNeutralButton(getString(R.string.cancel), null)
                    .show()
        }

//        viewModel.liveGame.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                Log.i("gillian64", "live game change ${it.box}")
//            }
//        })

        return binding.root
    }
}