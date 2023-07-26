package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.ActivityAppBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println("current token: $it")
        }

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }
                    .show()
                return@let
            }
            findNavController(R.id.container).navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { textArg = text }
            )
        }

        checkGoogleApiAvailability()

        val authViewModel: AuthViewModel by viewModels()
        var currentMenuProvider: MenuProvider? = null
        authViewModel.data.observe(this) {token ->
            val authorized = token.token != null

            currentMenuProvider?.let {
                removeMenuProvider(it)
            }
            addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.auth_menu, menu)
                        if(authorized){
                            menu.setGroupVisible(R.id.authorized, true)
                            menu.setGroupVisible(R.id.unauthorized, false)
                        }else{
                            menu.setGroupVisible(R.id.authorized, false)
                            menu.setGroupVisible(R.id.unauthorized, true)
                        }
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                        when(menuItem.itemId) {
                            R.id.signIn -> {
                                findNavController(R.id.nav_main).navigate(R.id.signInFragment)
                                true
                            }
                            R.id.signUp -> {
                                true
                            }
                            R.id.logout -> {
                                AppAuth.getInstance().remove()
                                true
                            }
                            else -> false
                        }
                }.also {
                    currentMenuProvider = it
                },
                this
            )
        }
    }


    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    }
}