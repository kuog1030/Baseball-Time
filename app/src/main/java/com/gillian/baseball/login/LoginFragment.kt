package com.gillian.baseball.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.R
import com.gillian.baseball.data.User
import com.gillian.baseball.databinding.FragmentLoginBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.TeamViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasUser()) viewModel.fetchTeam()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.buttonLoginGoogle.setOnClickListener {
            login()
        }

        binding.buttonLoginBroadcast.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigationToAllBroadcast())
        }

        // login -> intent -> viewModel.signInWithGoogle -> get firebase user
        // -> check if user exists (yes) -> fetch Team -> navigate to team
        //                         (no)  -> proceed login flow
        viewModel.userExist.observe(viewLifecycleOwner, Observer {
            it?.let { exist ->
                if (exist) {
                    viewModel.fetchTeam(true)
                } else {
                    val newUser = viewModel.createNewUser(viewModel.firebaseUser.value!!)
                    findNavController().navigate(LoginFragmentDirections.actionLoginToFirst(newUser))
                }
            }
        })

        viewModel.initTeam.observe(viewLifecycleOwner, Observer {
            it?.let {
                UserManager.team = it
                findNavController().navigate(NavigationDirections.navigationToTeam())
            }
        })


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.signInWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Log.w("gillian", "Google sign in failed : $e")

            }
        }
    }


    private fun login() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val singInIntent = googleSignInClient.signInIntent
        startActivityForResult(singInIntent, SIGN_IN)
    }


    private fun hasUser(): Boolean {
        return (UserManager.userId != "" && UserManager.teamId != "")
    }


    companion object {
        const val SIGN_IN = 0x99
    }


}