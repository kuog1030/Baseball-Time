package com.gillian.baseball.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.data.*
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.event.EventDialog
import com.gillian.baseball.game.onbase.OnBaseDialog
import com.gillian.baseball.game.pinch.PinchDialog

class GameFragment : Fragment() {


//    private val debugGame = MyGame(isHome = false, benchPlayer = mutableListOf(EventPlayer(playerId = "MF", order = -1, name = "代打1號",number = "11"),
//            EventPlayer(playerId = "MG", order = -1, name = "代打2號", number = "12"),
//            EventPlayer(playerId = "MH", order = -1, name = "代打3號", number = "18")),
//            game = Game(id = "xDyxGmZgD7bnjT2F2Pgw", title = "第四周測試賽", date = 1622708617703,
//                    place = "測試橋",
//                    home = GameTeam(name = "中信兄弟", acronym = "兄弟", teamId = "", image = "",
//                            pitcher = EventPlayer(playerId = "C", order = -1, name = "黃恩賜", number = "45"),
//                            lineUp = mutableListOf(EventPlayer(playerId = "A", order = 100, name = "第1棒", number = ""),
//                                    EventPlayer(playerId = "B", order = 200, name = "第2棒", number = ""),
//                                    EventPlayer(playerId = "C", order = 300, name = "第3棒", number = ""),
//                                    EventPlayer(playerId = "D", order = 400, name = "第4棒", number = ""),
//                                    EventPlayer(playerId = "E", order = 500, name = "第5棒", number = ""))),
//
//                    guest = GameTeam(name = "Lion", acronym = "Lion", teamId = "LZKevMu4xqIkpmQc7JLM", image = "",
//                            pitcher = EventPlayer(playerId = "4RRBLoRvDtJFzI5OGkO7", order = 100, name = "Stephan", number = "22"),
//                            lineUp = mutableListOf(EventPlayer(playerId = "4RRBLoRvDtJFzI5OGkO7", order = 100, name = "Stephan", number = "22"),
//                                    EventPlayer(playerId = "FLtzHQkWKReNn7sCa0tF", order = 200, name = "Curry", number = "38"),
//                                    EventPlayer(playerId = "MC", order = 300, name = "王柏融", number = "9"),
//                                    EventPlayer(playerId = "MD", order = 400, name = "陳鏞基", number = "13"),
//                                    EventPlayer(playerId = "ME", order = 500, name = "張偉聖", number = "52"),
//                                    EventPlayer(playerId = "ME", order = 600, name = "陳重羽", number = "65"),
//                                    EventPlayer(playerId = "ME", order = 700, name = "蘇智傑", number = "32"),
//                                    EventPlayer(playerId = "ME", order = 800, name = "林安可", number = "77"),
//                                    EventPlayer(playerId = "ME", order = 900, name = "潘武雄", number = "55")
//                            )),
//                    box = Box(score = mutableListOf(0), run = mutableListOf(0, 0), hit = mutableListOf(0, 0), error = mutableListOf(0, 0)),
//                    note = "", status = 1, recordedTeamId = "LZKevMu4xqIkpmQc7JLM"))



    //private val viewModel by viewModels<GameViewModel> {getVmFactory(debugGame) }
   private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToEvent.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Success")
                viewModel.onEventNavigated()
            }
        })

        viewModel.navigateToOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Out")
                viewModel.onOutNavigated()
            }
        })

        viewModel.navigateToOnBase.observe(viewLifecycleOwner, Observer {
            it?.let {
                val onBaseDialog = OnBaseDialog(it)
                onBaseDialog.show(childFragmentManager, "OnBase")
                viewModel.onOnBaseNavigated()
            }
        })

        viewModel.navigateToFinal.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToFinalFragment(it))
                viewModel.onFinalNavigated()
            }
        })

        viewModel.navigateToPinch.observe(viewLifecycleOwner, Observer {
            it?.let{
                val pinchDialog = PinchDialog(viewModel.isTop == viewModel.isHome)
                pinchDialog.show(childFragmentManager, "pinch")
                viewModel.onPinchNavigated()
            }
        })


        return binding.root
    }
}