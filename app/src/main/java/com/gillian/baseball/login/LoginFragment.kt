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
    private lateinit var auth: FirebaseAuth
    private lateinit var newUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasUser()) {
            Log.i("gillian6", "has user")
            viewModel.fetchTeam()
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // login -> intent -> viewModel.signInWithGoogle -> get firebase user
        // -> check if user exists (yes) -> navigate to team
        //                         (no)  -> proceed login flow
        binding.buttonLoginGoogle.setOnClickListener {
            login()
        }

        binding.buttonLoginBroadcast.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigationToAllBroadcast())
        }


        viewModel.initTeam.observe(viewLifecycleOwner, Observer {
            it?.let {
                UserManager.team = it
                Log.i("gillian67", "fetch team success ${UserManager.team}")
                findNavController().navigate(NavigationDirections.navigationToTeam())
            }
        })


        viewModel.firebaseUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                newUser = User(email = it.email!!, id = it.uid, image = it.photoUrl.toString())
                viewModel.searchIfUserExist()
            }
        })

        viewModel.userExist.observe(viewLifecycleOwner, Observer {
            it?.let{exist ->
                if (exist) {
                    // navigate
                    findNavController().navigate(NavigationDirections.navigationToTeam())
                } else {
                    findNavController().navigate(LoginFragmentDirections.actionLoginToFirst(newUser))
                    // navigate
                }
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
                Log.d("gillianlog", "firebaseAuthWithGoogle : ${account.id} and ${account.idToken}")
                viewModel.signInWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Log.w("gillianlog", "Google sign in failed : $e")

            }
        }
    }


    private fun login() {

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("534551205378-86bj9g48tc1mi74a8gnep6muval9hrdm.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val singInIntent = googleSignInClient.signInIntent
        startActivityForResult(singInIntent, SIGN_IN)
        Log.i("gillianlog", "login function called gso $gso")

    }


    private fun hasUser() : Boolean {
        Log.i("gillian67", "user id ${UserManager.userId}, team id ${UserManager.teamId}")
        return (UserManager.userId != "" && UserManager.teamId != "")
    }


    companion object {
        const val SIGN_IN = 0x99
    }


}