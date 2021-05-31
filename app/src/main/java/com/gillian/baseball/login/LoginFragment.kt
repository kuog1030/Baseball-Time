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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasUser()) {
            Log.i("gillianlog", "has user")
            //findNavController().navigate(NavigationDirections.navigationToTeam())
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        Log.i("gillianlog", "auth $auth currentUser $currentUser")


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel


        // login -> intent -> viewModel.signInWithGoogle -> get firebase user
        // -> create user in firebase database
        binding.buttonLoginGoogle.setOnClickListener {
            login()
        }

        viewModel.firebaseUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("gillian", "url? ${it.photoUrl}")
                viewModel.searchIfUserExist()
            }
        })

        viewModel.userExist.observe(viewLifecycleOwner, Observer {
            it?.let{ playerId->
                if (playerId.isEmpty()) {
                    Log.i("gillian","player id is empty")
                    //viewModel.newUserSignUp()
                } else {
                    UserManager.playerId = playerId
                    Log.i("gillian", "player id is not empty and usermanger player id is set")
                    viewModel.getTeamAndPlayer(playerId)
                    // use UserManager.playerId to search for team info then navigate to team

                }
            }
        })

        viewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("gillian", "user sign up success in firebase and $it")
                //UserManager.userId = it.id
                //Log.i("gillianlog", "usermanager id set ${UserManager.userId}")
                //viewModel.onFirstLoginNavigated()
            }
        })


        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            it?.let{
                UserManager.teamId = it.id
                UserManager.team = it
                Log.i("gillian", "get team by player id success $it")
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


    private fun hasUser() : Boolean{
        return (UserManager.userId != "")
    }


    companion object {
        const val SIGN_IN = 0x99
    }


}