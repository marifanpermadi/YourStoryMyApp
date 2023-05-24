package com.example.yourstorymyapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.yourstorymyapp.R
import com.example.yourstorymyapp.databinding.ActivityRegisterBinding
import com.example.yourstorymyapp.presentation.utils.Result
import com.example.yourstorymyapp.presentation.utils.UserPreferences
import com.example.yourstorymyapp.presentation.viewModel.RegisterViewModel
import com.example.yourstorymyapp.presentation.viewModel.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.registerButton.setOnClickListener {

            val emailEditText = binding.edRegisterEmail
            val passwordEditText = binding.edRegisterPassword

            if (!emailEditText.isEmailValid(emailEditText.text.toString())) {
                Toast.makeText(this, getString(R.string.email_invalid), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!passwordEditText.isPasswordValid()) {
                Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            register()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val addStoryMenu = menu.findItem(R.id.menu_add)
        val logOut = menu.findItem(R.id.menu_logout)
        val mapMenu = menu.findItem(R.id.menu_map)
        mapMenu.isVisible = false
        addStoryMenu.isVisible = false
        logOut.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return true
    }

    private fun register() {
        binding.apply {
            val name = nameEditText.text.toString()
            val email = edRegisterEmail.text.toString()
            val password = edRegisterPassword.text.toString()

            registerViewModel.register(name, email, password)
            showLoading(true)
            registerViewModel.register.observe(this@RegisterActivity) {
                showLoading(false)
                when (it) {
                    is Result.Success -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.register_succeed),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Result.Failure -> {
                        Toast.makeText(
                            this@RegisterActivity, it.message.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}