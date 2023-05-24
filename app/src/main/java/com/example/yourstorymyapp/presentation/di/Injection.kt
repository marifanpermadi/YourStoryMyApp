package com.example.yourstorymyapp.presentation.di

import com.example.yourstorymyapp.data.api.ApiConfig
import com.example.yourstorymyapp.domain.repository.Repository

object Injection {

    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()

        return Repository(apiService)
    }
}