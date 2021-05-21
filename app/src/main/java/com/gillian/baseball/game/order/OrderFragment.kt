package com.gillian.baseball.game.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gillian.baseball.NavigationDirections
import java.util.Collections.swap
import com.gillian.baseball.databinding.FragmentOrderBinding
import com.gillian.baseball.ext.getVmFactory

class OrderFragment : Fragment() {


    private val viewModel by viewModels<OrderViewModel> {getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentOrderBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = OrderAdapter()
        binding.recyclerOrderPlayers.adapter = adapter


        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerOrderPlayers)


        viewModel.submitList.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("gillian", "success and ${viewModel.lineUp}")

                adapter.submitList(viewModel.lineUp)
                viewModel.submitList.value = null
            }
        })


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
                findNavController().navigate(NavigationDirections.navigationToNewPlayer())
                viewModel.onNewPlayerDialogShowed()
            }
        })

        return binding.root
    }

    private var simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0){
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            var startPosition = viewHolder.adapterPosition
            var endPosition = target.adapterPosition

            swap(viewModel.lineUp, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }
}