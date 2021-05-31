package com.gillian.baseball.loginflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.databinding.FragmentLoginSearchBinding
import com.gillian.baseball.ext.getVmFactory

class LoginSearchFragment : Fragment() {

    private val viewModel by viewModels<LoginSearchViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginSearchBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerSearchTeams.adapter = SearchTeamAdapter(SearchTeamAdapter.OnClickListener{team ->
            findNavController().navigate(LoginSearchFragmentDirections.actionSearchToGet(team))
        })

        viewModel.navigateToCreate.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(LoginSearchFragmentDirections.actionSearchToCreate())
            }
        })

        return  binding.root
    }

}