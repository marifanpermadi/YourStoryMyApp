package com.example.yourstorymyapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.yourstorymyapp.R
import com.example.yourstorymyapp.data.api.ApiConfig
import com.example.yourstorymyapp.data.model.Auth
import com.example.yourstorymyapp.data.model.LoginResponse
import com.example.yourstorymyapp.databinding.ActivityLoginBinding
import com.example.yourstorymyapp.presentation.utils.UserPreferences
import com.example.yourstorymyapp.presentation.viewModel.MainViewModel
import com.example.yourstorymyapp.presentation.viewModel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val emailEditText = binding.edLoginEmail
        val passwordEditText = binding.edLoginPassword

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (!emailEditText.isEmailValid(emailEditText.text.toString())) {
                Toast.makeText(this, getString(R.string.email_invalid), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!passwordEditText.isPasswordValid()) {
                Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]
    }

    private fun login(email: String, password: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "Response: $responseBody")
                if (response.isSuccessful && responseBody?.message == "success") {
                    mainViewModel.saveUser(Auth(responseBody.loginResult.token, true))
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e(TAG, "Response failed: ${response.message()}")
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_fail),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "Main Activity"
    }

}