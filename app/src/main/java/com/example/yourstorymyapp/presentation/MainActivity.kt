package com.example.yourstorymyapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourstorymyapp.R
import com.example.yourstorymyapp.databinding.ActivityMainBinding
import com.example.yourstorymyapp.presentation.utils.LoadingStateAdapter
import com.example.yourstorymyapp.presentation.utils.StoryAdapter
import com.example.yourstorymyapp.presentation.utils.UserPreferences
import com.example.yourstorymyapp.presentation.viewModel.MainViewModel
import com.example.yourstorymyapp.presentation.viewModel.StoryViewModel
import com.example.yourstorymyapp.presentation.viewModel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModel.ViewModelFactory()
    }
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
        binding.rvStories.setHasFixedSize(true)

        loading = binding.progressBar

        getStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_logout -> {
                mainViewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                return true
            }
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]
    }

    private fun getStories() {
        val adapter = StoryAdapter()
        loading.visibility = View.VISIBLE
        binding.rvStories.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })

        mainViewModel.getUser().observe(this) {
            if (it != null) {

                storyViewModel.stories("Bearer " + it.token).observe(this) { stories ->
                    loading.visibility = View.GONE
                    adapter.submitData(lifecycle, stories)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    override fun onBackPressed() {
        finish()
    }
}