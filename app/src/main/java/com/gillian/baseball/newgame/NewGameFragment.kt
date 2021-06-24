package com.gillian.baseball.newgame

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.R
import com.gillian.baseball.allgames.AllGamesViewModel
import com.gillian.baseball.data.Team
import com.gillian.baseball.databinding.FragmentNewGameBinding
import com.gillian.baseball.ext.getVmFactory
import java.text.SimpleDateFormat
import java.util.*

class NewGameFragment : Fragment() {

    private val viewModel by viewModels<NewGameViewModel> {getVmFactory(NewGameFragmentArgs.fromBundle(requireArguments()).myTeam) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNewGameBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.buttonNewGameDate.setOnClickListener {
            pickDateTime()
        }

        viewModel.selectedSideRadio.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.updateCard()
            }
        })

        viewModel.awayName.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.updateCard()
            }
        })

        viewModel.scheduleSuccess.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(NewGameFragmentDirections.actionNewGameToAllGames())
                viewModel.onGameUploadedAndNavigated()
            }
        })

        return binding.root
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), R.style.dateTimePickerDialog, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), R.style.dateTimePickerDialog, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                viewModel.setUpDate(pickedDateTime.timeInMillis)

            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }
}