package com.example.yourstorymyapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourstorymyapp.data.model.Auth
import com.example.yourstorymyapp.presentation.utils.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getUser(): LiveData<Auth> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: Auth) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}