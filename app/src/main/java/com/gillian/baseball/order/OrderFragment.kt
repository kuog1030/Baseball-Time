package com.gillian.baseball.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.data.EventPlayer
import java.util.Collections.swap
import com.gillian.baseball.databinding.FragmentOrderBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.newplayer.NewPlayerDialog
import com.gillian.baseball.order.OrderFragmentDirections

class OrderFragment : Fragment() {

    private val args: OrderFragmentArgs by navArgs()
    private val viewModel by viewModels<OrderViewModel> {getVmFactory(args.scheduleGame) }
    lateinit var binding : FragmentOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = OrderAdapter()
        binding.recyclerOrderPlayers.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerOrderPlayers)

        // submit list after fetching team players
        viewModel.submitList.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(viewModel.lineUp)
                setUpSpinnerAdapter(viewModel.pitcherList)

                viewModel.submitList.value = null
            }
        })

        // set up game in firebase
        viewModel.setUpGame.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.navigateToGame(it)
            }
        })

        viewModel.navigateToGame.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(OrderFragmentDirections.actionOrderFragmentToGameFragment(it))
                viewModel.onGameNavigated()
            }
        })

        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
            it?.let{
                val newPlayerDialog = NewPlayerDialog(false)
                newPlayerDialog.show(childFragmentManager, "")
                viewModel.onNewPlayerDialogShowed()
            }
        })

        return binding.root
    }

    private var simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0){
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            swap(viewModel.lineUp, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            recyclerView.adapter?.notifyItemChanged(startPosition)
            recyclerView.adapter?.notifyItemChanged(endPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }

    private fun setUpSpinnerAdapter(list: List<EventPlayer>) {
        val spinnerAdapter = PitcherSpinnerAdapter(requireContext(), list)
        binding.spinnerOrderPitcher.adapter = spinnerAdapter
        binding.spinnerOrderPitcher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectPitcher(position)
            }
        }
    }
}