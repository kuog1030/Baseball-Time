package com.gillian.baseball.statistics.statplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.databinding.FragmentStatPlayerBinding
import com.gillian.baseball.editplayer.EditPlayerDialog
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class StatPlayerFragment : Fragment() {

    private val viewModel by viewModels<StatPlayerViewModel> { getVmFactory() }
    private lateinit var binding: FragmentStatPlayerBinding
    private val args: StatPlayerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentStatPlayerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.fetchPlayerStat(args.playerId)

        binding.buttonStatPlayerLeave.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonStatPlayerShare.setOnClickListener {
            val teamName = UserManager.team?.name ?: ""
            val messageBody = BaseballApplication.instance.getString(R.string.share_player_id, teamName, args.playerId)
            sharePlayerId(getString(R.string.share_subject), messageBody, getString(R.string.share_title))
        }

        binding.buttonStatPlayerDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.CustomAlertDialog)
                .setTitle(getString(R.string.delete_user_confirm))
                .setMessage(getString(R.string.delete_user))
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    viewModel.deleteUser()
                }
                .setNeutralButton(getString(R.string.cancel), null)
                .show()
        }

        viewModel.player.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.hitStat = it.hitStat
                binding.pitchStat = it.pitchStat
            }
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer {
            it?.let {
                val editPlayerDialog = EditPlayerDialog(it)
                editPlayerDialog.show(childFragmentManager, "")
                viewModel.onEditNavigated()
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let{
                UserManager.userId = ""
                UserManager.teamId = ""
                UserManager.playerId = ""
                UserManager.team = null
                findNavController().navigate(StatPlayerFragmentDirections.actionStatPlayerFragmentToLoginFragment())
            }
        })

        val fadeInAnim = Util.getAnim(R.anim.fade_in_anim)

        viewModel.infoVisibility.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.textStatPlayerInfo.startAnimation(fadeInAnim)
                } else {
                    binding.textStatPlayerInfo.clearAnimation()
                }
            }
        })

        return binding.root
    }

    private fun sharePlayerId(subject: String, body: String, chooserTitle: String) {
        val sharingIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.type = "text/plain"
            this.putExtra(Intent.EXTRA_SUBJECT, subject)
            this.putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(Intent.createChooser(sharingIntent, chooserTitle))
    }
}