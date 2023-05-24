package com.example.yourstorymyapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.yourstorymyapp.data.model.ListStoryItem
import com.example.yourstorymyapp.domain.repository.Repository
import com.example.yourstorymyapp.presentation.di.Injection

class StoryViewModel(private val repository: Repository) : ViewModel() {

    fun stories(header: String): LiveData<PagingData<ListStoryItem>> =
        repository.getDataForPaging(header).cachedIn(viewModelScope)

    class ViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StoryViewModel(Injection.provideRepository()) as T
            } else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}