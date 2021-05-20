package com.gillian.baseball.newgame

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gillian.baseball.databinding.FragmentNewGameBinding
import com.gillian.baseball.ext.getVmFactory
import java.util.*

class NewGameFragment : Fragment() {

    lateinit var datePickerDialog: DatePickerDialog
    private val viewModel by viewModels<NewGameViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNewGameBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, y, m, d ->
                viewModel.formatDate(y, m, d)
        }, year, month, day)


        binding.buttonNewGameDate.setOnClickListener {
            datePickerDialog.show()
        }


        viewModel.selectedSideRadio.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.changeSide()
            }
        })


        return binding.root
    }

    private fun initDatePicker() {

    }
}